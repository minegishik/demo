/**
 * 
 * エラーチェック
 * 
 */

 
//目のアイコンを表示
function togglePasswordVisibility() {
	var passwordField = document.getElementById("textPassword");
	var eyeIcon = document.getElementById("buttonEye");

	if (passwordField.type === "password") {
		passwordField.type = "text";
		eyeIcon.classList.remove("fa-eye");
		eyeIcon.classList.add("fa-eye-slash");
	} else {
		passwordField.type = "password";
		eyeIcon.classList.remove("fa-eye-slash");
		eyeIcon.classList.add("fa-eye");
	}
}


//ログインフォームのエラーチェック

function validateForm() {
	var userId = document.getElementById("textUserId").value.trim();
	var password = document.getElementById("textPassword").value.trim();
	var errorMessages = "ユーザーID、パスワードが不正、もしくはユーザーが無効です。";

	// ログインIDチェック
	if (userId === "") {
		errorMessages.push(errorMessages);
	}
	//文字数チェック
	if (userId.length > 16) {
		errorMessages.push(errorMessages);
	}
	// 全角文字をチェック
	if (/[\u3000-\u303F\u4E00-\u9FFF\uFF00-\uFFEF]/.test(userId)) { 
		errorMessages.push(errorMessages);
	}


	// パスワードチェック
	if (password === "") {
		errorMessages.push(errorMessages);
	}
	// パスワードの文字数チェック
	if (password.length > 16) {
		errorMessages.push(errorMessages);
	}
	// パスワードの全角文字チェック
	if (/[\u3000-\u303F\u4E00-\u9FFF\uFF00-\uFFEF]/.test(password)) {
		errorMessages.push(errorMessages);
	}


	// エラーメッセージがある場合
	if (errorMessages.length > 0) {
		displayErrors(errorMessages); // エラーメッセージの表示を専用の関数に委任
		return false; // フォームの送信をキャンセル
	}
	return true; // フォームを送信
}





