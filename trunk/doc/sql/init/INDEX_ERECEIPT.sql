



CREATE NONCLUSTERED INDEX I_INDEX_BILL_SERIALNUMBER ON T_ERECEIPT_BILL(easSerialNumber)


CREATE NONCLUSTERED INDEX I_INDEX_BILL_FLAG_PAYTIME ON T_ERECEIPT_BILL(flag,payFinishTime)


CREATE NONCLUSTERED INDEX I_INDEX_REQUEST ON T_ERECEIPT_REQUEST(flag,failTimes,bankCode)


CREATE NONCLUSTERED INDEX I_INDEX_UPLOAD ON T_ERECEIPT_UPLOAD(billId)