const main = document.getElementById('main');
const contentsOl = document.querySelector('.post-container > ol');
const contentsLoadOffset = 100; // 가장 화면 최하단으로부터 100px 정도 떨어진 곳에 도착했다면 새로 컨텐츠를 로드

load_posts(99999999);
// 메인화면에서 스크롤 할 때 이벤트
main.onscroll = scroll;

function show_post(postNo){
    const objectTag = document.createElement('object');
    objectTag.data = `/post/${postNo}`;
    document.body.appendChild(objectTag);
}

function scroll(){
    // 최하단에서 100px 정도 떨어진 위치로 왔다
    if(main.scrollTop + document.body.offsetHeight >= main.scrollHeight - contentsLoadOffset){
        const article = main.getElementsByTagName('article');
        const lastArticleId = article.item(article.length - 1).id;
        load_posts(lastArticleId);
        // 한번 로딩 하고 나면 스크롤 이벤트 없애라
        main.onscroll = null;
    }
}

function load_posts(lastArticleNo){
    console.log('load_posts 실행..')
    if(lastArticleNo <= 1){
        console.log('마지막 게시물');
        return; // fetch 요청 안보내고 종료
    }
    const xhr = new XMLHttpRequest();
    xhr.onloadstart = () => {
        console.log('로딩 시작..');
    }
    xhr.onprogress = () => {
        console.log('로딩중...');
    }
    xhr.onload = () => {
        console.log('로딩 완료!');
        const posts = JSON.parse(xhr.responseText);
        if(posts.length !== 0){
            // 스크롤 이벤트 다시 걸어줌
            main.onscroll = scroll;
        }
        for(const post of posts){
            const postContent = post.contents[0];
            let postContentObject = ''; // 요소가 img인가 video인가
            if(postContent.mimeType === 'video') {
                postContentObject = `<video src="/contents/${postContent.uuid}"></video>`;
            }
            else if (postContent.mimeType === 'image') {
                postContentObject = `<img src="/contents/${postContent.uuid}" alt="image" />`;
            }
            let postContentSticker = '';
            if(post.contents.length > 1){
                postContentSticker = `<span><i class="bi bi-stickies"></i></span>`;
            }
            contentsOl.insertAdjacentHTML(`beforeend`,
                `<li id="${post.no}" onclick="show_post(${post.no})">
                    <button>
                        ${postContentObject}
                        ${postContentSticker}
                    </button>
                    <div>
                        <div>
                            <i class="bi bi-heart-fill"></i>
                            <span>9</span>
                        </div>
                        <div>
                            <i class="bi bi-chat-fill"></i>
                            <span>11</span>
                        </div>
                    </div>
                </li>`
            );
        }
    }
    xhr.open('GET', `/contents?lastPostNo=${lastArticleNo}&count=12`);
    xhr.send(null); // GET 요청에서는 body가 null이여야 함
}