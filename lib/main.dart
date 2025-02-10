import 'package:flutter/material.dart';
import 'nfc_service.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: Text('NFC Tap Demo')),
        body: Center(
          child: ElevatedButton(
            onPressed: () async {
              await NFCService.startNfcService(context);
            },
            child: Text("Tap NFC"),
          ),
        ),
      ),
    );
  }
}
