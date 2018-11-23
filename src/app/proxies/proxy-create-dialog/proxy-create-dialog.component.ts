import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToxiproxyService} from '../../services/toxiproxy.service';
import {MatDialogRef, MatSnackBar} from '@angular/material';
import {Proxy} from '../../services/proxy';

@Component({
  selector: 'app-proxy-create-dialog',
  templateUrl: './proxy-create-dialog.component.html',
  styleUrls: ['./proxy-create-dialog.component.scss']
})
export class ProxyCreateDialogComponent implements OnInit {

  inProgress: boolean | false;
  form: FormGroup;

  constructor(private dialog: MatDialogRef<ProxyCreateDialogComponent>,
              private fb: FormBuilder,
              private proxyService: ToxiproxyService,
              private snackBar: MatSnackBar) {

    this.form = this.fb.group({
      name: ['', Validators.required],
      listen: ['', Validators.required],
      upstream: ['', Validators.required],
      enabled: 'true'
    });
  }

  ngOnInit() {
  }

  onSubmit() {
    this.inProgress = true;
    const proxy = new Proxy();
    proxy.name = this.form.get('name').value;
    proxy.listen = this.form.get('listen').value;
    proxy.upstream = this.form.get('upstream').value;
    proxy.enabled = this.form.get('enabled').value == 'true';

    this.proxyService
      .createProxy(proxy)
      .subscribe(
        () => {
          this.inProgress = false;
          this.dialog.close();
        },
        () => {
          this.inProgress = false;
          this.snackBar.open(
            'Unable to create proxy. Proxy name and listen ports must be unique.',
            'Close',
            {duration: 8000});
        },
        () => this.inProgress = false
      );
  }
}
