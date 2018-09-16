import {Injectable} from "@angular/core";
import {TranslateService} from "@ngx-translate/core";

export interface Marker {
  lat: number;
  lng: number;
  label?: string;
  draggable: boolean;
}

export interface Location {
  lat: number;
  lng: number;
  viewport?: Object;
  zoom: number;
  address_level_1?: string;
  address_level_2?: string;
  address_country?: string;
  address_zip?: string;
  address_state?: string;
  marker?: Marker;
}

export const defaultZoom = 5;


@Injectable({
  providedIn: 'root',
})
export class MapService {

  constructor(private translate: TranslateService) {
  }
}
