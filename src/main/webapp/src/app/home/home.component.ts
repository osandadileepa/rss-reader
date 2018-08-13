import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {tap} from "rxjs/operators";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  private urlList: string [] = [];

  public items : object [] = [];
  public rssFeeds : { string , object};

  constructor(private http: HttpClient) {
  }

  ngOnInit() {
    this.urlList.push('http://tech.uzabase.com/rss');
    this.readRssFeed();
  }

  private readRssFeed(): void {

    let url = environment.basePath + 'rss/read';

    let params: HttpParams = new HttpParams();
    params = params.append('urlList', this.urlList.toString());

    let readSub = this.http.get(url, {params}).pipe(
      tap((data: any) => {

        this.rssFeeds = <{string, object}>data;

        this.urlList.forEach( url => {
          this.items.push(this.rssFeeds[url]);
        });

      })
    ).subscribe(
      (value: any) => {
        readSub.unsubscribe();
      });

  }// readRssFeed()

  public downloadFile () {

    let url = environment.basePath + 'rss/download';
    window.open(url);

  }// downloadFile()

}
