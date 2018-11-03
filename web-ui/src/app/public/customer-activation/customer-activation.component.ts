import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../common/http-service/user.service";
import {HttpErrorResponse} from "@angular/common/http";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'customer-activation',
  templateUrl: './customer-activation.component.html',
  styleUrls: ['./customer-activation.component.scss'],
  providers: [UserService]
})
export class CustomerActivationComponent implements OnInit {

  messageTitle: string;
  messageContent: string;

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private translate: TranslateService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => this.confirmActivation(params['id']));
  }

  confirmActivation(activationKey: string) {
    this.translate.get('customerActivation').subscribe((res: any) => {
      const title = res['title'];
      const content = res['content'];
      this.userService.activateCustomer(activationKey).subscribe(
        () => {
          this.messageTitle = title.success;
          this.messageContent = content.success;
        },
        (errorResponse: HttpErrorResponse) => {
          this.messageTitle = title.fail;
          this.messageContent = content[errorResponse.error[0].code];
        });
    });
  }
}
