import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";

export class Address {
  constructor(public street: string,
              public city: string,
              public latitude: number,
              public longitude: number) {
  }
}

export class SportObject {
  constructor(public id: string,
              public sportsclubId: string,
              public name: string,
              public description: string,
              public address: Address,
              public imageUrl: string) {
  }
}

@Injectable()
export class SportObjectService {

  constructor(private http: HttpClient) {
  }

  get(success: (objects: SportObject[]) => void, fail: () => void) {
    let url = `${environment.apiUrl}/public-api/sportsclub/${environment.sportsclubId}/sport-object`;

    this.http.get<SportObject[]>(url).subscribe(
      (data: SportObject[]) => {
        success(data);
      },
      () => {
        fail();
      });
  }
}
