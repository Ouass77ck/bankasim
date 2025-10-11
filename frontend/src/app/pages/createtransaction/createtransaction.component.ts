import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MaterialModule } from '../../shared/material.module';
import { ApiService } from '../../services/api.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-createtransaction',
  standalone: true,
  imports: [CommonModule, FormsModule, MaterialModule],
  templateUrl: './createtransaction.component.html',
  styleUrls: ['./createtransaction.component.scss']
})

export class CreatetransactionComponent {
  senderId = 'c348cbfb-309c-4142-81ca-b550aca32200';
  ribDestinataire = 'FR112935952411316125131513';
  montant = 0;

  constructor(private api: ApiService, private snack: MatSnackBar) {}

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
}
