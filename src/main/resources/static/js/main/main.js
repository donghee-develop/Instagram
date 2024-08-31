const main = document.getElementById('main');
const contentsLoadOffset = 100; // 가장 화면 최하단으로부터 100px 정도 떨어진 곳에 도착했다면 새로 컨텐츠를 로드
const mainSection = document.getElementById('main-section');

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
        console.log('load....');
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
        // xhr.responseText: 서버가 response로 응답한 body 데이터를 text 형태로 가져옴
        mainSection.insertAdjacentHTML(`beforeend`, xhr.responseText);
        // 생성된 post들에게 캐러셀 적용
        const emblaNodes = mainSection.getElementsByClassName('embla');
        [...emblaNodes].forEach(emblaNode => {
            embla_init(emblaNode)
        });
        // 스크롤 이벤트 다시 걸어줌
        main.onscroll = scroll;
    }
    xhr.open('GET', `/posts?lastPostNo=${lastArticleNo}`);
    xhr.responseType = 'text'; // html이 오기 때문에 응답은 text형태
    xhr.send(null); // GET 요청에서는 body가 null이여야 함
}

/*************************************************************/





