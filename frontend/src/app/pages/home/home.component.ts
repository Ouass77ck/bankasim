import { Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { MaterialModule } from '../../shared/material.module';
import { ApiService } from '../../services/api.service';
import { RouterLink, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  imports: [CommonModule, MaterialModule, RouterModule, CurrencyPipe, MatMenuModule, MatButtonModule, MatToolbarModule, RouterLink],
})
export class HomeComponent implements OnInit {
  account: any;
  userId :string | null;

  constructor(private api: ApiService, private auth: AuthService) {
      this.userId=this.auth.getUserId();
  }

  ngOnInit() {
    this.api.getAccountById(this.userId).subscribe({
      next: (res) => this.account = res,
      error: (err) => console.error(err)
    });
  }
  logout() {
    this.auth.logout();
  }
}
