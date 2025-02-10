import 'dart:convert';
import 'package:flutter_nfc_kit/flutter_nfc_kit.dart';
import 'package:http/http.dart' as http;

class NFCService {
  static Future<void> startNfcService(dynamic TypeNameFormat) async {
    try {
      // Ambil member_id dari API Laravel
      String memberId = await fetchMemberId();
      print("Member ID: $memberId");

      // Tunggu hingga NFC reader mendeteksi
      NFCTag tag = await FlutterNfcKit.poll(timeout: Duration(seconds: 10));

      if (tag.ndefAvailable ?? false) {
        // Konversi ke format NDEF yang sesuai
       NDEFRawRecord record = NDEFRawRecord(
          [] as String, // identifier kosong
          utf8.encode('\u0002en$memberId') as String, // Payload
          utf8.encode('T') as String, // Tipe record (Text)
          TypeNameFormat.nfcWellKnown, // Format NDEF yang benar
        );


        // Kirim data ke NFC
        await FlutterNfcKit.writeNDEFRawRecords([record]);

        print("Data NFC terkirim: $memberId");
      } else {
        print("Tag tidak mendukung NDEF.");
      }
    } catch (e) {
      print("Error NFC: ${e.toString()}");
    }
  }

  static Future<String> fetchMemberId() async {
    try {
      String token = "YOUR_TOKEN_ACCESS";
      

      final response = await http.get(
        Uri.parse("YOUR_API_URL"),
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer $token",
        },
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        return data['profile']['member_id'].toString();
      } else {
        return "Error: ${response.statusCode}";
      }
    } catch (e) {
      return "Error: ${e.toString()}";
    }
  }
}
