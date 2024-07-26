/**
 * 勤怠機能
 * 
 * @param year 年
 * @param month 月
 * 
 */

//登録ボタンの非活性化
function validateAttendanceInput() {
    var tableRows = document.querySelectorAll("#attendanceTableBody tr"); // 勤怠情報が表示されるテーブルの行を取得

    for (var i = 0; i < tableRows.length; i++) {
        var select = tableRows[i].querySelector("select.form-control"); // 勤務状況のセレクトボックスを取得

        if (!select || select.value === "") { // セレクトボックスが存在しないか、選択がされていない場合
            alert("勤怠情報をすべて入力してください。");
            return false; // 送信をキャンセル
        }
    }

    return true; // すべての勤怠情報が入力されている場合、送信を許可
}

//日付のフォーマット変換
function formatDateString(dateString) {
    var date = new Date(dateString);
    return (date.getMonth() + 1) + "/" + date.getDate();
}


//曜日の表示
function formatDayOfWeek(dateString) {
    var date = new Date(dateString);
    var daysOfWeek = ["日", "月", "火", "水", "木", "金", "土"];
    return daysOfWeek[date.getDay()];
}


