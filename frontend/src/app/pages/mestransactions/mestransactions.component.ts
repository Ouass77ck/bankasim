import { Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { MaterialModule } from '../../shared/material.module';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-mestransactions',
  standalone: true,
  imports: [CommonModule, MaterialModule, CurrencyPipe],
  templateUrl: './mestransactions.component.html',
  styleUrls: ['./mestransactions.component.scss']
})
export class MestransactionsComponent implements OnInit {
  transactions: any[] = [];
  displayedColumns = ['id', 'ribDestinataire', 'montant'];
  userId = 'c348cbfb-309c-4142-81ca-b550aca32200';

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.api.getTransactionsByUser(this.userId).subscribe({
      next: (res) => this.transactions = res,
      error: (err) => console.error(err)
    });
  }
}
