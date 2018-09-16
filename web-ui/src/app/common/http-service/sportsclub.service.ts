import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {environment} from "../../../environments/environment";

// export class Address {
//   constructor(public street: string,
//               public city: string,
//               public latitude: number,
//               public longitude: number) {
//   }
// }

export class Address {
  constructor(public latitude: number,
              public longitude: number) {
  }
}

export class Sportsclub {
  constructor(public id: string,
              public name: string,
              public description: string,
              public address: Address) {
  }
}

@Injectable()
export class SportsclubService {

  private static readonly sportsclubStorageKey = 'sportsclub';

  private readonly sportsclubPublicApi: string =
    `${environment.apiUrl}/public-api/sportsclub/${environment.sportsclubId}`;

  constructor(private http: HttpClient) {
  }

  get(successCallback: (sportsclub: Sportsclub) => void,
      errorCallback: (errorResponse: HttpErrorResponse) => void): void {
    const sportsclub = SportsclubService.tryToGetSportsclubFromSession();

    if (sportsclub) {
      successCallback(sportsclub);
    } else {
      this.http.get<Sportsclub>(this.sportsclubPublicApi).subscribe(
        (sportsclub) => {
          sessionStorage.setItem(SportsclubService.sportsclubStorageKey, JSON.stringify(sportsclub));
          successCallback(sportsclub);
        },
        (error) => {
          errorCallback(error);
        }
      );
    }
  }

  private static tryToGetSportsclubFromSession(): Sportsclub {
    const sportsclub = JSON.parse(sessionStorage.getItem(SportsclubService.sportsclubStorageKey));
    console.log(`tryToGetSportsclubFromSession: ${sportsclub}`);
    if (!sportsclub || sportsclub.length == 0) {
      console.log('session storage empty');
      return undefined;

    } else {
      return sportsclub;
    }
  }
}
