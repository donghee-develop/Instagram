function like_post(postNo, likeBtn){
    fetch(`/post/like/${postNo}`, {
        method: "POST",
        headers: {"X-CSRF-TOKEN": csrfToken}
    }).then(response => {
        if(response.status === 201){
            likeBtn.querySelector('i').className = 'bi bi-heart-fill';
            window.parent.postMessage('like', '*');
        }
    });
}
function like_cancel_post(postNo, likeBtn){
    fetch(`/post/like/${postNo}`, {
        method: "DELETE",
        headers: {"X-CSRF-TOKEN": csrfToken}
    }).then(response => {
        if(response.ok){
            likeBtn.querySelector('i').className = 'bi bi-heart';
            window.parent.postMessage('like_cancel', '*');
        }
    });
}
