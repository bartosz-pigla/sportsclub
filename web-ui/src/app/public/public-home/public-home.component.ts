import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../../common/security/authentication.service";

@Component({
  selector: 'app-home-page',
  templateUrl: './public-home.component.html',
  styleUrls: ['./public-home.component.scss']
})
export class PublicHomeComponent implements OnInit {

  constructor(private authenticationService: AuthenticationService) {
  }

  ngOnInit() {
    console.log(`home: is signed in: ${this.authenticationService.isSignedIn()}`);
  }
}
