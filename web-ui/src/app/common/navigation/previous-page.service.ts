import {Injectable} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {DefaultPathService} from "./default-path.service";

@Injectable({
  providedIn: 'root',
})
export class PreviousPageService {

  constructor(private router: Router,
              private defaultPathService: DefaultPathService) {
  }

  navigate(activatedRoute: ActivatedRoute) {
    activatedRoute.queryParams.subscribe(params => {
      let previousUrl = params.previousUrl;

      if (previousUrl) {
        this.router.navigate([previousUrl]);
      } else {
        this.router.navigate([this.defaultPathService.navigate()]);
      }
    });
  }
}
