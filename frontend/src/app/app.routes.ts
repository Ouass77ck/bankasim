import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { CreatetransactionComponent } from './pages/createtransaction/createtransaction.component';
import { MestransactionsComponent } from './pages/mestransactions/mestransactions.component';
import { LoginComponent } from './pages/login/login.component';

import { AuthGuard } from './auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'createtransaction', component: CreatetransactionComponent, canActivate: [AuthGuard] },
  { path: 'mestransactions', component: MestransactionsComponent, canActivate: [AuthGuard] },
];

