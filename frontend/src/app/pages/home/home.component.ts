import { Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { MaterialModule } from '../../shared/material.module';
import { ApiService } from '../../services/api.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  imports: [CommonModule, MaterialModule, RouterModule, CurrencyPipe],
})
export class HomeComponent implements OnInit {
  account: any;
  userId = 'c348cbfb-309c-4142-81ca-b550aca32200';

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.api.getAccountById(this.userId).subscribe({
      next: (res) => this.account = res,
      error: (err) => console.error(err)
    });
  }
}
