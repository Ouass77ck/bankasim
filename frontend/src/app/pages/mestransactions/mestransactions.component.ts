import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../../shared/material.module';
import { ApiService } from '../../services/api.service';
import { AuthService } from '../../services/auth.service';
import { forkJoin } from 'rxjs';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterLink } from '@angular/router';


@Component({
  selector: 'app-mestransactions',
  standalone: true,
  imports: [CommonModule, MaterialModule, MatMenuModule, MatButtonModule, MatToolbarModule, RouterLink],
  templateUrl: './mestransactions.component.html',
  styleUrls: ['./mestransactions.component.scss']
})
export class MestransactionsComponent implements OnInit {
  transactions: any[] = [];
  displayedColumns = ['ribDestinataire', 'montant', 'status', 'description'];
  userId: string | null;
  account: any;
  rib: string | null = null;

  constructor(private api: ApiService, private auth: AuthService) {
    this.userId = this.auth.getUserId();
  }

  ngOnInit() {
    if (!this.userId) {
      console.error('Utilisateur non authentifié');
      return;
    }
    this.api.getAccountById(this.userId).subscribe({
      next: (res) => this.account = res,
      error: (err) => console.error(err)
    });

    this.api.getAccountById(this.userId).subscribe({
      next: (account) => {
        this.rib = account.rib;
        forkJoin([
          this.api.getTransactionsByUser(this.userId),
          this.api.getTransactionsByRibDestinataire(this.rib)
        ]).subscribe({
          next: ([sent, received]) => {
            // On marque chaque transaction avec son type
            const sentTransactions = sent.map(t => ({
              ...t,
              type: 'envoyée',
              montantAffiche: `- ${t.montant}€`,
              ribAffiche: t.ribDestinataire
            }));

            const receivedTransactions = received.map(t => ({
              ...t,
              type: 'reçue',
              montantAffiche: `+ ${t.montant}€`,
              ribAffiche: 'Moi'
            }));
            this.transactions = [...sentTransactions, ...receivedTransactions]
              .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());
          },
          error: (err) => console.error('Erreur lors du chargement des transactions', err)
        });
      },
      error: (err) => console.error('Erreur lors de la récupération du compte', err)
    });
  }
  logout() {
    this.auth.logout();
  }
}
