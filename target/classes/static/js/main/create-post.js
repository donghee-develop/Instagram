const form = document.forms.item(0);
const createBtn = document.getElementById('create-btn');

const createPostContainer = document.getElementById('create-post-container');
const createPostCloseBtn = document.getElementById('create-post-close-btn');

const imageViewerSection = document.querySelector('.image-viewer-section');
const imageViewerSectionFigureTag = imageViewerSection.querySelector('figure');

const imageFileViewer = document.getElementById('image-file-viewer');
const imageFileViewerOnOffBtn = document.getElementById('image-file-viewer-on-off-btn');

const imageFilePlusView = imageFileViewer.querySelector('li')
const imageFilePlusFileInput = imageFilePlusView.querySelector('input[type=file]');
const imageFilePlusBtn = imageFilePlusView.querySelector('label');

const spinnerProgressContainer = document.getElementById('spinner-container');
const spinnerProgressText = spinnerProgressContainer.querySelector('b');

// 게시물 게시하기 버튼 클릭 => form 내용을 가져와서 전송
createBtn.onclick = () => {
    if(!confirm('게시하시겠습니까?')){
        return; // 종료
    }
    const formData = new FormData(form);
    const csrfToken = form.querySelector('input[name=_csrf]').value;

    const xhr = new XMLHttpRequest();
    let uploadProgress = 0; // 현재 업로드 진척도
    // upload가 실행되었을 때 실행되는 이벤트
    xhr.upload.onloadstart = () => {
        // spinner를 화면에 보여주도록 한다
        spinnerProgressContainer.toggleAttribute('active', true);
    }
    // upload가 진행중일때 실행되는 이벤트
    xhr.upload.onprogress = event => {
        console.log('xhr.upload.onprogress');
        setInterval(() => {
            // 현재 진척도 = (현재로딩된값 / 전체로딩해야할값 * 100) %
            uploadProgress = event.loaded / event.total * 100;
            spinnerProgressText.textContent = `${uploadProgress}%...`;
        }, 100); // 100ms 마다 한번씩 동작
    }
    // 업로드가 완료되었을 경우 실행되는 이벤트
    xhr.upload.onload = () => {
        // 1초 뒤에 실행되도록 함
        setTimeout(() => {
            alert('게시 완료!');
            window.parent.postMessage('post', '*');
        }, 1000);
    }

    // fetch 처럼 경로 지정
    xhr.open('POST', `/post/create`, true);
    xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
    // formData를 전송
    xhr.send(formData);
}



// 닫기 버튼 클릭
createPostCloseBtn.onclick = () => {
    if(confirm('등록을 취소하시겠습니까?')){
        window.parent.postMessage('remove', '*');
    }
}
// Image viewer ON / OFF
imageFileViewerOnOffBtn.onclick = function () {
    imageFileViewer.toggleAttribute('active');
}

imageFilePlusFileInput.onchange = () => {
    const file = imageFilePlusFileInput.files[0];
    const fileType = file.type.toString();
    if(fileType.includes('image')){
        create_image_file_view(`<img alt="">`);
    }
    else if(fileType.includes('video')){
        create_image_file_view(`<video loop muted autoplay></video>`);
    }
    // imageFilePlusFileInput.value = null;
    imageFilePlusFileInput.files = null;
};
function create_image_file_view(tag){
    /**************************************************/
    // object URL 생성
    const objectURL = URL.createObjectURL(imageFilePlusFileInput.files[0]);
    // 새로운 PLUS 요소 생성 후 큰 화면 변경
    const newImageFilePlusView = imageFilePlusView.cloneNode(true);
    newImageFilePlusView.querySelector('input[type=file]').name = 'contents';
    imageFileViewer.insertBefore(newImageFilePlusView, imageFilePlusView);
    newImageFilePlusView.setAttribute('active', ''); // 뷰어 화면 변경
    imageViewerSection.setAttribute('active', ''); // 뷰어 화면 변경
    imageViewerSectionFigureTag.innerHTML = tag;
    imageViewerSectionFigureTag.firstElementChild.src = objectURL; // 뷰어 태그의 데이터 설정

    // 작은 화면 설정
    const figureTag = newImageFilePlusView.querySelector('figure');
    figureTag.innerHTML = tag;
    const objectTag = figureTag.firstElementChild;
    objectTag.src = objectURL;

    // 이미지 보기 클릭
    newImageFilePlusView.onclick = (e) => {
        e.preventDefault();
        e.stopPropagation();
        imageViewerSectionFigureTag.innerHTML = tag;
        const imageViewerSectionObjectTag = imageViewerSectionFigureTag.firstElementChild;
        imageViewerSectionObjectTag.src = objectTag.src; // 뷰어 태그의 데이터 설정
        imageViewerSection.setAttribute('active', ''); // 뷰어 화면 변경
    };

    // 삭제 버튼 클릭 시
    const rmBtn = newImageFilePlusView.querySelector('button');
    rmBtn.onclick = (e) => {
        e.stopPropagation();
        e.preventDefault();
        if(confirm('정말 삭제하시겠습니까?')){
            newImageFilePlusView.remove();
            const activatedViews = document.querySelectorAll('.image-file-view[active]');
            if(activatedViews.length === 0){
                imageViewerSectionFigureTag.firstElementChild.src = null;
                imageViewerSection.removeAttribute('active');
            }else{
                const lastObjectTag = activatedViews[activatedViews.length - 1].querySelector('figure').firstElementChild;
                imageViewerSectionFigureTag.firstElementChild.remove();
                imageViewerSectionFigureTag.appendChild(lastObjectTag.cloneNode(true));
                imageViewerSectionFigureTag.firstElementChild.src = lastObjectTag.src;
            }
        }
    };
}