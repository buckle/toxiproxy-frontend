import {ToxicType} from './toxic-type';
import {AbstractControl} from '@angular/forms';

export module ToxicTypeConstants {

  export const LATENCY = <ToxicType> {
    'name': 'Latency',
    'value': 'latency',
    'attributes': [
      {'name': 'Latency', 'value': 'latency', 'dataType': 'number', 'tip': 'Time in milliseconds'},
      {'name': 'Jitter', 'value': 'jitter', 'dataType': 'number', 'tip': 'Time in milliseconds'}
    ]
  };
  export const BANDWIDTH = <ToxicType> {
    'name': 'Bandwidth',
    'value': 'bandwidth',
    'attributes': [
      {'name': 'Rate', 'value': 'rate', 'dataType': 'number', 'tip': 'Rate in KB/s'}
    ]
  };
  export const SLOW_CLOSE = <ToxicType> {
    'name': 'Slow Close',
    'value': 'slow_close',
    'attributes': [
      {'name': 'Delay', 'value': 'delay', 'dataType': 'number', 'tip': 'Time in milliseconds'}
    ]
  };
  export const TIMEOUT = <ToxicType> {
    'name': 'Timeout',
    'value': 'timeout',
    'attributes': [
      {'name': 'Timeout', 'value': 'timeout', 'dataType': 'number', 'tip': 'Time in milliseconds'}
    ]
  };
  export const SLICER = <ToxicType> {
    'name': 'Slicer',
    'value': 'slicer',
    'attributes': [
      {'name': 'Average Size', 'value': 'average_size', 'dataType': 'number', 'tip': 'Size in bytes of an average packet'},
      {
        'name': 'Size Variation',
        'value': 'size_variation',
        'dataType': 'number',
        'tip': 'Variation in bytes of an average packet (should be smaller than average size)'
      },
      {'name': 'Delay', 'value': 'delay', 'dataType': 'number', 'tip': 'Time in microseconds to delay each packet by'}
    ]
  };
  export const LIMIT_DATA = <ToxicType> {
    'name': 'Limit Data',
    'value': 'limit_data',
    'attributes': [
      {'name': 'Bytes', 'value': 'Bytes', 'dataType': 'number', 'tip': 'Number of bytes it should transmit before connection is closed'}
    ]
  };

  export function getToxicTypes(): ToxicType[] {
    return [this.LATENCY, this.BANDWIDTH, this.SLOW_CLOSE, this.TIMEOUT, this.SLICER, this.LIMIT_DATA];
  }

  export function getToxicTypeByValue(value: string): ToxicType {
    const foundTypes = this.getToxicTypes().filter(typeValue => typeValue.value === value);
    return foundTypes.length > 0 ? foundTypes[0] : null;
  }

  export function convertFormAttributesToDataType(toxicType: ToxicType, formAttributes: AbstractControl): object {
    const attributes = {};

    toxicType.attributes.forEach(attribute => {
      const attributeFormControl = formAttributes.get(attribute.value);

      switch(attribute.dataType) {
        case 'number':
          attributes[attribute.value] = Number(attributeFormControl.value).valueOf();
          break;
      }
    });

    return attributes;
  }
}


