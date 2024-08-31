const csrfToken = document.querySelector('input[name=_csrf]').value;
// 이메일 입력란
const emailInput = document.getElementById('email-input');
// 닉네임 입력란
const nicknameInput = document.getElementById('nickname-input');
// 인증번호 발송 버튼
const emailAuthBtn = document.getElementById('email-auth-btn');
// 인증번호 입력 input
const emailAuthInput = document.getElementById('email-auth-input');
// 회원가입 버튼
const submitBtn = document.querySelector('button[type=submit]');
// 인증번호 발송 버튼 클릭 시
emailAuthBtn.onclick = () => {
    const email = emailInput.value.trim();
    if(email.length === 0){
        alert('이메일을 입력해주세요');
        return;
    }

    alert('이메일에 인증번호를 발송했습니다!');
    fetch(`/user/auth`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": csrfToken
        },
        // 입력된 이메일로 인증번호 발송
        body: email
    }).then(response => {
        console.log(response)
        if(response.status === 200){ // 발송 성공

            emailAuthBtn.disabled = true; // 더 이상 인증 못하게
        }else{
            alert('이메일 전송 중 오류가 발생였습니다. 다시 한번 확인해주세요.');
        }
    })
}
// 이메일 입력 시 이메일 검사
emailInput.onchange = () => {
    const emailDuplicateMessageDiv = document.getElementById('email-duplicate-message');
    const email = emailInput.value.trim();
    fetch(`/user/check?email=${email}`)
        .then(response => {
            if (response.status === 409) {
                emailDuplicateMessageDiv.toggleAttribute('active', true);
            }
            else{
                emailDuplicateMessageDiv.toggleAttribute('active', false);

            }
        });
}
// 닉네임 입력 시 검사
nicknameInput.onchange = () => {
    const nicknameDuplicateMessageDiv = document.getElementById('nickname-duplicate-message');
    const nickname = nicknameInput.value.trim();
    fetch(`/user/check?nickname=${nickname}`)
        .then(response => {
            if (response.status === 409) {
                nicknameDuplicateMessageDiv.toggleAttribute('active', true);
            }
            else{
                nicknameDuplicateMessageDiv.toggleAttribute('active', false);
            }
        });
}

// submitBtn.onclick = (e) => {
//     e.preventDefault();
//     const form = document.forms.item(0); // form 을 가져온다
// }










