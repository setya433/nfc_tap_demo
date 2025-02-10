from smartcard.System import readers
from smartcard.util import toHexString

# Mendeteksi NFC Reader
r = readers()
if not r:
    print("Tidak ada NFC Reader yang terdeteksi!")
    exit()

reader = r[0]
print(f"Menggunakan reader: {reader}")

# Buat koneksi dengan reader
try:
    connection = reader.createConnection()
    connection.connect()
    print("Berhasil terhubung ke NFC Reader!")
except Exception as e:
    print(f"Gagal terhubung ke NFC Reader: {e}")
    exit()

# APDU untuk memilih AID
APDU_SELECT_AID = [0x00, 0xA4, 0x04, 0x00, 0x07] + [0xF0, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06]

try:
    # Kirim SELECT AID
    response, sw1, sw2 = connection.transmit(APDU_SELECT_AID)

    # Konversi response ke string
    response_str = "".join(chr(b) for b in response)
    
    print(f"Response: {response_str} | SW1={hex(sw1)}, SW2={hex(sw2)}")

    if sw1 == 0x90 and sw2 == 0x00:
        print(f"Data dari NFC: {response_str}")
    else:
        print("Gagal mendapatkan data NFC")
except Exception as e:
    print(f"Kesalahan saat mengirim APDU: {e}")
