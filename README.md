# Fortune-City-to-Spendria-Record-Converter
將記帳城市輸出的csv檔轉換為Spendria可以接受的json檔\
\
記帳城市網址：\
https://fortunecityapp.com/zh-TW/ \
Spendria Google Play網址：\
https://play.google.com/store/apps/details?id=com.rocketapp101.finance\


## 如何使用

### 必要檔案：

安裝 Java Runtime Environment 1.8.0_191 或以上\
https://www.oracle.com/technetwork/java/javase/downloads/index.html \
確認系統路徑有JRE在裡面

下載最新版的程式壓縮檔(內含jar檔、Spendria.json)\
https://github.com/nh60211as/Fortune-City-to-Spendria-Record-Converter/releases\

### 操作步驟
從記帳城市中匯出記錄檔(需要訂閱，可以開啟14天免費試用但是要記得取消訂閱)

Windows 7、Windows 10 命令提示字元：
```
cd <FC2S.jar 的目錄>
java -jar FC2S.jar <匯出的記帳城市紀錄.csv> <要輸出的位置Spendria記錄.json>
```
接下來會顯示無法辨別的記帳種類
```
Category "居家" is never seen before
Press 1 to set this category to EXPENSE
Press 2 to set this category to INCOME
```
輸入1設定為消費，輸入2設定為收入\
結束程式後開啟Spendria並將輸出的json檔於設定>您的資料>清除並還原開啟\
???\
大功告成，記得檢查有沒有任何錯誤\
