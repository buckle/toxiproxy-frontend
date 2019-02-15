import {ToxicTypeConstants} from './toxic-type-constants';
import {FormControl, FormGroup} from '@angular/forms';

describe('ToxicTypeConstants', () => {

  it('should get toxic type by value', () => {
    let toxicTypeByValue = ToxicTypeConstants.getToxicTypeByValue(ToxicTypeConstants.LATENCY.value);

    expect(toxicTypeByValue).toEqual(ToxicTypeConstants.LATENCY);
  });

  it('should get toxic type by value when none found', () => {
    let toxicTypeByValue = ToxicTypeConstants.getToxicTypeByValue('nope');

    expect(toxicTypeByValue).toEqual(null);
  });

  it('should convert attributes to data type', () => {

    let attributes = ToxicTypeConstants.convertFormAttributesToDataType(
      ToxicTypeConstants.LATENCY,
      new FormGroup({latency: new FormControl('100'), jitter: new FormControl('101')}));

    expect(attributes).toBeTruthy();
    expect(attributes['latency']).toEqual(100);
    expect(attributes['jitter']).toEqual(101);
  });

});
