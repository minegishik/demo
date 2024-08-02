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
	var errorMessages = [];

	// ログインIDチェック
	if (userId === "") {
		errorMessages.push("ログインIDを入力してください。");
	} else {
		if (userId.length > 16) {
			errorMessages.push("ログインIDは16文字以内で入力してください。");
		}
		if (/[\u3000-\u303F\u4E00-\u9FFF\uFF00-\uFFEF]/.test(userId)) { // 全角文字をチェック
			errorMessages.push("ログインIDは半角文字で入力してください。");
		}
	}

		// パスワードチェック
		if (password === "") {
			errorMessages.push("パスワードを入力してください。");
		} else {
			// パスワードの文字数チェック
			if (password.length > 16) {
				errorMessages.push("パスワードは16文字以内で入力してください。");
			}
			// パスワードの全角文字チェック
			if (/[\u3000-\u303F\u4E00-\u9FFF\uFF00-\uFFEF]/.test(password)) {
				errorMessages.push("パスワードは半角文字で入力してください。");
			}
		}

		// エラーメッセージがある場合
		if (errorMessages.length > 0) {
			var errorList = document.getElementById("error-list");
			errorList.innerHTML = ""; // 既存のエラーメッセージをクリア

			errorMessages.forEach(function(message) {
				var li = document.createElement("li");
				li.innerText = message;
				errorList.appendChild(li);
			});

			return false; // フォームの送信をキャンセル
		}
		return true; // フォームを送信
	}



//ログインIDが全角文字・16文字を超える場合のエラーメッセージ

