import { Component, OnInit} from '@angular/core';
import { DataService } from "../data.service";

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css']
})
export class FormComponent implements OnInit {

  constructor(private dataService: DataService) { }

  ngOnInit(): void {
  }

  valueCheck():boolean {
    let lowPriceStr = (<HTMLInputElement>document.getElementById('lowPrice')).value
    let highPriceStr = (<HTMLInputElement>document.getElementById('highPrice')).value
    let lowPrice = parseFloat(lowPriceStr)
    let highPrice = parseFloat(highPriceStr)
    if (lowPrice < 0.0) {
      alert("Price Range values cannot be negative! Please try a value greater than or equal to 0.0")
      return false
    }
    else if (lowPrice > highPrice) {
      alert("Oops! Lower price limit cannot be greater than upper price limit! Please try again.")
      return false
    }
    else return true
  }
  
  data: any;
  dataLength: number;

  callFetch():void {
    if (this.valueCheck()) {
      // str:String = String.toString(formData)
      let form = document.getElementsByTagName('form')[0]
      let formData = new FormData(form)
      
      //@ts-ignore
      let params = new URLSearchParams(formData)
      console.log(params.toString())
      let req = new XMLHttpRequest()
      req.open("GET", "/q?" + params, false)
      req.send();
      let res = req.responseText
      let obj = JSON.parse(res)

      // this.dataService.setData(obj)
      this.data = obj.searchResult.item
      this.dataLength = this.data.length
    }
  }
}


