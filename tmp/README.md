参考：https://wbwangk.github.io/hyperledgerDocs/write_first_app_zh/

tx.json是新增CAR10:
```javascript
var request = {
  //targets: let default to the peer assigned to the client
  chaincodeId: 'fabcar',
  fcn: 'createCar',
  args: ['CAR10', 'Chevy', 'Volt', 'Red', 'Nick'],
  chainId: 'mychannel',
  txId: tx_id
};
```
tx2.json是修改CAR10拥有者：
```javascript
const request = {
  //targets : --- letting this default to the peers assigned to the channel
  chaincodeId: 'fabcar',
  fcn: 'queryCar',
  args: ['CAR10']
};
```
block4.json是4号区块的信息。这个区块包含了一个交易，即tx2.json这个交易。
