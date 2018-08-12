import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {map} from "rxjs/operators";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  private urlList : string [] = [];

  constructor(private http : HttpClient) { }

  ngOnInit() {
    this.urlList.push('http://tech.uzabase.com/rss');
    this.readRssFeed();
  }

  private readRssFeed() : void{

    let url = environment.basePath + 'rss/read';

    let params: HttpParams = new HttpParams();
    params.append('urlList', this.urlList.toString());

    let readSub = this.http.get(url, {params}).pipe(
      map((data :any) => {
        console.log('Map ', data);
      })
    ).subscribe(
      (value : any) => {
        readSub.unsubscribe();
      });

  }// readRssFeed()

}
