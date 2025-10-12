import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import {jwtDecode} from 'jwt-decode';

interface DecodedToken {
  sub?: string;
  role?: string;
  exp?: number;
  iat?: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8081/api/auth';
  private decodedToken: DecodedToken | null = null;

  constructor(private http: HttpClient) {
    const token = this.getToken();
    if (token) this.decodeToken(token);
  }

  login(credentials: { email: string; password: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, credentials).pipe(
      tap((response: any) => {
        if (response && response.token) {
          localStorage.setItem('token', response.token);
          this.decodeToken(response.token);
        }
      })
    );
  }

  logout() {
    localStorage.removeItem('token');
    this.decodedToken = null;
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  private decodeToken(token: string) {
    try {
      this.decodedToken = jwtDecode(token);
    } catch (e) {
      console.error('Erreur de décodage du token', e);
      this.decodedToken = null;
    }
  }

  /** Récupère l'UUID du user depuis le token */
  getUserId(): string | null {
    if (!this.decodedToken) {
      const token = this.getToken();
      if (token) this.decodeToken(token);
    }
    return this.decodedToken?.sub || null;
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getUserRole(): string | undefined {
    return this.decodedToken?.role;
  }
}
