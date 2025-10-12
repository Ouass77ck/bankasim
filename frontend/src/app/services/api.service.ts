import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrlAccount = 'http://localhost:8080/api/accounts';
  private baseUrlTransaction = 'http://localhost:8082/api/transactions'

  constructor(private http: HttpClient) {}

  // --- Account
  getAccountById(id: string | null): Observable<any> {
    return this.http.get(`${this.baseUrlAccount}/${id}`);
  }

  // --- Transactions
  createTransaction(payload: any): Observable<any> {
    return this.http.post(`${this.baseUrlAccount}/transfer`, payload);
  }

  getTransactionsByUser(userId: string | null): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrlTransaction}/user/${userId}`);
  }

  getTransactionsByRibDestinataire(rib: string | null): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrlTransaction}/rib/${rib}`);
  }


}
