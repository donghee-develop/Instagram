const paths = location.pathname.split('/');
const roomId = paths[paths.length - 1];
/**********************************************/
const messageListContainer = document.querySelector('.message-list-container');
const roomList = messageListContainer.querySelectorAll('li');

const client = new StompJs.Client();
client.brokerURL = 'ws://localhost:8080/chatting';
client.onConnect = function (frame) {
    console.log('서버와 연결이 완료되었습니다');
    client.subscribe(`/topic/${roomId}`, response => {
        const message = JSON.parse(response.body);
        if(roomId === message.roomId){
            create_chat_message(message);
        }
        create_chat_room_last_message(message);
    });
    for(const room of roomList) {
        client.subscribe(`/topic/${room.id}`, response => {
            const message = JSON.parse(response.body);
            if(roomId === message.roomId){
                create_chat_message(message);
            }
            create_chat_room_last_message(message);
        });
    }
}
client.activate();

const chatContainer = document.querySelector('.chat-container');

// 내가 들어와있는 채팅방에(메인) 메세지를 표시
function create_chat_message(message){
    const sender = message?.sender?.email;
    console.log('sender', sender)
    if(sender === null){
        alert('로그인???');
        return;
    }
    const lastMessageDiv = chatContainer.querySelector('.message:last-child');
    // 메세지를 이 방에서 처음 주고받음
    if(lastMessageDiv === null){
        return;
    }
    const lastMessageDivClasses = lastMessageDiv.classList;
    console.log(lastMessageDivClasses)
    // 마지막에 보낸 사람과 방금 받은 메세지 보낸 사람이 같다
    /// === 그냥 span만 추가해줌
    if(lastMessageDivClasses.contains(sender)){
        const messageContainer = lastMessageDiv.querySelector('.message-container');
        messageContainer.insertAdjacentHTML(`beforeend`, `<span>${message.message}</span>`);
    }
    // div를 새로 만들고 위에 있는 div의 반대쪽편에 만들어줘야함
    else{
        const meOrOther = lastMessageDivClasses.contains('me') ? 'other' : 'me';
        let profileContainer = '';
        if(meOrOther === 'other'){
            profileContainer = `<div class="user-profile-img-container">
                <img src="/image/${sender}" alt="#"/>
            </div>`
        }

        chatContainer.insertAdjacentHTML(`beforeend`,
            `<div class="${meOrOther} message ${sender}">
                ${profileContainer}
                <div class="message-container">
                    <span>${message.message}</span>
                </div>
            </div>`
        )
    }
}

// 내가 들어와있는 모든 방에서(사이드바) 현재 온 메세지를 표시
function create_chat_room_last_message(message){
    // 사이드바에서 해당 방의 목록을 찾는다
    const roomLi = document.getElementById(message.roomId);
    // 새로운 방에서 채팅이 입력됨
    if(roomLi === null){
        // 사이드바에 전부 표시해야함
        messageListContainer.insertAdjacentHTML(`beforeend`
        `
            <li id="${message.roomId}" onclick="location.href='/chat/${message.roomId}'">
                <div class="user-profile-img-container">
                    <img src="/images/${message.sender.email}" alt="#"/>
                </div>
                <div class="user-profile-info-container">
                    <div class="user-nickname">${message.sender.nickname}</div>
                    <div class="message-container">
                        <span class="message">${message.message}</span>
                        <span class="dot">·</span>
                        <span class="message-time">16시간</span>
                    </div>
                </div>
        </li>
        
        `)
    }
    const roomMessageSpan = roomLi.querySelector('.message');
    roomMessageSpan.textContent = message.message;
}

/************************************************************************/
const chatTextInput = document.getElementById('chat-text-input');
if(chatTextInput !== null){
    chatTextInput.onkeydown = e => {
        if(e.key === 'Enter'){
            client.publish({
                destination: `/app/${roomId}`, // 메세지를 보내는 목적지 경로
                body: JSON.stringify({
                    message: chatTextInput.value
                }) // 전달할 메세지 데이터
            })
        }
    }
}

/***********************************************************/
const chatWriteContainer = document.getElementById('chat-write-container');
const chatWriteBtn = document.getElementById('chat-write-btn');
const chatWriteCloseBtn = document.querySelector('.chat-write-close-btn');
const userSearchInput = document.getElementById('user-search-input');
const userListContainer = document.querySelector('.user-list-container');
chatWriteBtn.onclick = () => {
    chatWriteContainer.setAttribute('active', '');
    search_users('');
}
chatWriteCloseBtn.onclick = () => {
    chatWriteContainer.removeAttribute('active');
    userSearchInput.value = '';
    userListContainer.innerHTML = '';
}

userSearchInput.onkeyup = () => {
    const keyword = userSearchInput.value;
    search_users(keyword);

}

function search_users(keyword){
    fetch(`/user/search?keyword=${keyword}`)
        .then(response => response.json())
        .then(users => {
            userListContainer.innerHTML = '';
            for(const user of users) {
                userListContainer.insertAdjacentHTML(`beforeend`,
                    `<li onclick="create_chat_room('${user.email}')">
                        <div class="user-profile-img-container">
                            <img src="/image/${user.email}" alt="#"/>
                        </div>
                        <div class="user-profile-info-container">
                            <span class="user-nickname">${user.nickname}</span>
                            <span class="user-name">${user.name}</span>
                        </div>
                    </li>`
                );
            }
        })
}

function create_chat_room(userEmail){
    const csrfToken =document.querySelector('meta[name=_csrf]').getAttribute('content');
    fetch(`/chat/create?user=${userEmail}`, {
        method: "POST",
        headers: {"X-CSRF-TOKEN": csrfToken},
    })
        .then(response => {
            // found
          if(response.status === 302){
              alert('이미 방이 존재합니다. 방으로 이동')
          }
          // created
          else if(response.status === 201){
              alert("방을 생성했습니다. 방으로 이동");
          }
          else{
              throw Error;
          }
          return response.text();

        })
        .then(roomId => {
            location.href= `/chat/${roomId}`;
        },
            reason => {
            }
            )
}









