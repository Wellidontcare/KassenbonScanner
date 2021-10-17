# KassenbonScanner

Dieser Kassenbonscanner scannt mit tesseract-ocr und ist ein proof of concept.

* Bild laden
* Scanbereich auswählen
* Bon scannen, fertig!

OCR Qualität kann stark schwanken!

## Installationsanleitung:
### Linux und Mac
```bash
git clone https://github.com/Wellidontcare/KassenbonScanner.git
cd KassenbonScanner
mnv clean javafx:run
```

### Windows (möglicherweise)
```bash
git clone https://github.com/Wellidontcare/KassenbonScanner.git
```
* Projekt mit intellij idea öffnen
* Neue run configuration typ maven hinzufügen
* command: clean javafx:run
