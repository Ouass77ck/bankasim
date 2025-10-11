import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCard } from '@angular/material/card';
import { MatFormField } from '@angular/material/input';
import { MatLabel } from '@angular/material/input';
import { AuthService } from '../../services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, MatInputModule, MatButtonModule,
    MatCard, MatFormField, MatLabel],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent {
  email = '';
  password = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private snack: MatSnackBar,
  ) {}

  onLogin() {
    this.authService.login({ email: this.email, password: this.password }).subscribe({
      next: () => {
        this.snack.open('Connexion réussie ✅', 'Fermer', { duration: 2000 });
        this.router.navigate(['/home']);
      },
      error: (err) => {
        console.error(err);
        this.snack.open('Identifiants incorrects ❌', 'Fermer', { duration: 3000 });
      }
    });
  }
}
