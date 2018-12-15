package ch.heigvd.iict.sym.a3dcompassapp;

/*
*
* Interface in order to simplify and factor code.
* Allow you to define the behaviour of your activity once the NFC tag is read
*
 */
public interface ActivityWithNFC {
    void doThisWhenNfcTagRead(String readingResult);
}
