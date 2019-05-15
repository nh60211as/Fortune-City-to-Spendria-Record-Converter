# Fortune-City-to-Spendria-Record-Converter
Transfer Fortune City Record to the formate Spendria support\
\
Fortune City Website：\
https://fortunecityapp.com/en \
Spendria on Google Play：\
https://play.google.com/store/apps/details?id=com.rocketapp101.finance\


## How To Use

### Requirement：

Install Java Runtime Environment 1.8.0_191 or higher\
https://www.oracle.com/technetwork/java/javase/downloads/index.html \
Make sure system path includes JRE

Download the newest realease (includes JAR and translation files)\
https://github.com/nh60211as/Fortune-City-to-Spendria-Record-Converter/releases

### Steps to Transfer Record
Export the record from Fortune City (requires subscription, you can activate 14 days free trial but remember to cancel it)

Windows 7、Windows 10 Command Prompt：
```
cd <folder of FC2S.jar>
java -jar FC2S.jar <Fortune City Record.csv> <language code> <Output Spendria Record.json>
```
< language code> is the code for your Fortune City language. Ex: \
java -jar FC2S.jar "Fortune City Record.csv" eng "Spendria Record.json" \
The supported languages are Traditional Chinese (chT), English (eng), Simplified Chinese (chS), Japanese (jpn), and Korean (kor)

If the program cannot recognize your category for spending, it will prompt you to select whether it's expenses or income
```
Category "Scholarship" is never seen before
Press 1 to set this category to EXPENSE
Press 2 to set this category to INCOME
```
After the program finished running, open up Spendria and go to setting>your data>clear and restore (I don't know what's the exact translation)\
???\
You're done. Remember to check if anything is wrong.


## Libraries Used
Gson to read and write json files\
https://github.com/google/gson \
Opencsv to read csv files\
http://opencsv.sourceforge.net/
