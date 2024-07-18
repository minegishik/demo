/**
 * 勤怠機能
 * 
 * @param year 年
 * @param month 月
 * 
 */
// JavaScriptで月の日数を計算する関数
function daysInMonth(year, month) {
	return new Date(year, month, 0).getDate();
}

// 表示ボタンをクリックした際の処理
function displayAttendance() {
	var year = parseInt(document.getElementById("year").value);
	var month = parseInt(document.getElementById("month").value);
	var days = daysInMonth(year, month); // 月の日数を計算

	var tableBody = document.getElementById("attendanceTableBody");
	tableBody.innerHTML = ""; // テーブルの内容をクリア

	// 曜日の配列
	var weekdays = ["日", "月", "火", "水", "木", "金", "土"];

	// テーブルに曜日と月日を追加
	for (var day = 1; day <= days; day++) {
		var row = document.createElement("tr");


		// 月日セルを作成
		var dateCell = document.createElement("td");
		dateCell.classList.add("w40");
		dateCell.textContent = month + "/" + day;
		row.appendChild(dateCell);

		// 曜日セルを作成
		var weekdayCell = document.createElement("td");
		weekdayCell.classList.add("w40");
		var date = new Date(year, month - 1, day);
		var weekday = weekdays[date.getDay()]; // 0が日曜なので -1する
		weekdayCell.textContent = weekday;
		row.appendChild(weekdayCell);


		// 勤務状況セル
		var attendanceCell = document.createElement("td");
		attendanceCell.classList.add("w100");
		var select = document.createElement("select");
		select.classList.add("form-control");
		
		// 勤務状況のオプションと対応する数値
		var options = [
			{ value: "", text: "選択してください" },
			{ value: 0, text: "通常出勤" },
			{ value: 1, text: "休日" },
			{ value: 2, text: "祝日" },
			{ value: 3, text: "遅刻" },
			{ value: 4, text: "有給" },
			{ value: 5, text: "欠勤" },
			{ value: 6, text: "早退" },
			{ value: 7, text: "時間外勤務" },
			{ value: 8, text: "振替出勤" },
			{ value: 9, text: "振替休日" },
			{ value: 10, text: "代替出勤" },
			{ value: 11, text: "代替休日" }
		];
		
		
		
		for (var i = 0; i < options.length; i++) {
			var option = document.createElement("option");
			option.value = options[i].value;
			option.textContent = options[i].text;
			select.appendChild(option);
		}
		
		attendanceCell.appendChild(select);
		row.appendChild(attendanceCell);

		// 出勤時間セル
		var startTimeCell = document.createElement("td");
		startTimeCell.classList.add("w120");
		var startTimeDiv = document.createElement("div");
		startTimeDiv.classList.add("time-selectors");

		// 時間のプルダウン
		var startHourSelect = document.createElement("select");
		startHourSelect.classList.add("form-control");
		startHourSelect.name = "startHour"; // 時間の選択肢の名前
		for (var hour = 0; hour < 24; hour++) {
			var option = document.createElement("option");
			var hourString = ("0" + hour).slice(-2);
			option.value = hourString;
			option.textContent = hourString;
			startHourSelect.appendChild(option);
		}

		// 分のプルダウン
		var startMinuteSelect = document.createElement("select");
		startMinuteSelect.classList.add("form-control");
		startMinuteSelect.name = "startMinute"; // 分の選択肢の名前
		for (var minute = 0; minute < 60; minute++) {
			var option = document.createElement("option");
			var minuteString = ("0" + minute).slice(-2);
			option.value = minuteString;
			option.textContent = minuteString;
			startMinuteSelect.appendChild(option);
		}

		// プルダウンを追加
		startTimeDiv.appendChild(startHourSelect);
		startTimeDiv.appendChild(startMinuteSelect);

		startTimeCell.appendChild(startTimeDiv);
		row.appendChild(startTimeCell);

		// 退勤時間セル
		var endTimeCell = document.createElement("td");
		endTimeCell.classList.add("w120");
		var endTimeDiv = document.createElement("div");
		endTimeDiv.classList.add("time-selectors");

		// 時間のプルダウン
		var endHourSelect = document.createElement("select");
		endHourSelect.classList.add("form-control");
		endHourSelect.name = "endHour"; // 時間の選択肢の名前
		for (var hour = 0; hour < 24; hour++) {
			var option = document.createElement("option");
			var hourString = ("0" + hour).slice(-2);
			option.value = hourString;
			option.textContent = hourString;
			endHourSelect.appendChild(option);
		}

		// 分のプルダウン
		var endMinuteSelect = document.createElement("select");
		endMinuteSelect.classList.add("form-control");
		endMinuteSelect.name = "endMinute"; // 分の選択肢の名前
		for (var minute = 0; minute < 60; minute++) {
			var option = document.createElement("option");
			var minuteString = ("0" + minute).slice(-2);
			option.value = minuteString;
			option.textContent = minuteString;
			endMinuteSelect.appendChild(option);
		}

		// プルダウンを追加
		endTimeDiv.appendChild(endHourSelect);
		endTimeDiv.appendChild(endMinuteSelect);

		endTimeCell.appendChild(endTimeDiv);
		row.appendChild(endTimeCell);


		// 備考セル
		var remarksCell = document.createElement("td");
		remarksCell.classList.add("w150");

		// テキストボックスの作成
		var remarksInput = document.createElement("input");
		remarksInput.type = "text";
		remarksInput.classList.add("form-control");

		remarksCell.appendChild(remarksInput);
		row.appendChild(remarksCell);
		
		

		tableBody.appendChild(row);
	}
		}