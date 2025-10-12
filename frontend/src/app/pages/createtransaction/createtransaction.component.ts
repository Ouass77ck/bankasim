import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MaterialModule } from '../../shared/material.module';
import { ApiService } from '../../services/api.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../services/auth.service';
import { RouterLink } from '@angular/router';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';

@Component({
  selector: 'app-createtransaction',
  standalone: true,
  imports: [CommonModule, FormsModule, MaterialModule, RouterLink, MatMenuModule, MatButtonModule, MatToolbarModule],
  templateUrl: './createtransaction.component.html',
  styleUrls: ['./createtransaction.component.scss']
})

export class CreatetransactionComponent implements OnInit{
  senderId : string | null;
  ribDestinataire = 'Rib destinataire';
  montant = 0;
  account: any;

  constructor(private api: ApiService, private snack: MatSnackBar, private auth: AuthService) {
      this.senderId=this.auth.getUserId();
  }

    ngOnInit(): void {
      this.api.getAccountById(this.senderId).subscribe({
      next: (res) => this.account = res,
      error: (err) => console.error(err)
    });   
    }
    createTransaction() {
    const payload = {
      senderId: this.senderId,
      ribDestinataire: this.ribDestinataire,
      montant: this.montant
    };
    this.api.createTransaction(payload).subscribe({
      next: () => this.snack.open('Virement effectué avec succès', 'Fermer', { duration: 3000 }),
      error: (err) => this.snack.open(err.error?.error || 'Erreur lors du virement', 'Fermer', { duration: 3000 })
    });
  }
  logout() {
    this.auth.logout();
  }
}
