import { Component, OnInit, Input } from '@angular/core';
import { DataService } from "../data.service";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {

  constructor(private dataService: DataService) { }

  ngOnInit(): void {
  }

  @Input() data: any

  displayData(): void {
    console.log(this.data)
  }

  p: number = 1;
  collection: any[] = ['a','a','a','a','a','a','a','b'];
}
