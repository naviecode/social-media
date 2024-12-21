let stompClient_main;

connectToWebSocket();

function connectToWebSocket() {
    const socket = new SockJS('/chat');
    stompClient_main = Stomp.over(socket);
    stompClient_main.connect({}, () => {
        subscribeChat();
    });

}

// subscrible websocket
function subscribeChat() {
    //Nhận thông báo từ nhóm chat
    if(groups != null){
        groups.forEach(function(item) {
            stompClient_main.subscribe('/topic/groups/'+ item.chatId, function (message) {
                const chatMessage = JSON.parse(message.body);
                if(chatMessage.senderId !== userLoginId){
                    showNotification('https://via.placeholder.com/50', chatMessage.content);
                }
            });
        });
    }
    //Nhận thông báo từ tương tác giữa người dùng
    stompClient_main.subscribe('/user/queue/notifications', function (message) {
        const chatMessage = JSON.parse(message.body);
        console.log("da duoc cai dat");
        showNotification('https://via.placeholder.com/50', chatMessage.content);
        renderNotify();
    });
}

function showNotification(avatarUrl, message) {
    const notification = document.getElementById('notification-container');
    const notificationAvatar = document.getElementById('notification-avatar');
    const notificationMessage = document.getElementById('notification-message');

    // Set avatar and message
    notificationAvatar.src = avatarUrl;
    notificationMessage.textContent = message;

    // Show notification
    notification.classList.remove('hidden');

    // Hide notification after 3 seconds
    setTimeout(() => {
        notification.classList.add('hidden');
    }, 3000);
}

function renderNotify(){
    $.ajax({
        url: '/notifications/user',
        method: 'GET',
        success: function(data) {
            let notificationPanelWrap = $('#notification-item-render');
            console.log(data);
            if(notificationPanelWrap){
                notificationPanelWrap.empty();
                data.forEach(function(notification) {

                    let avatarSrc = notification.avatarUrl ? '/static/files/' + notification.avatarUrl : '/static/files/empty_image.png';

                    let notificationItem = '';
                    if (notification.notifyType === 'personal') {
                        notificationItem = `
                        <div class="notification-item">
                            <a href="/user/${notification.senderId}" style="display: flex; text-decoration: none">
                                <img src="${avatarSrc}" alt="Profile picture" width="40" height="40">
                                <div class="text">
                                    <div class="title">${notification.content}</div>
                                    <div class="subtitle">${new Date(notification.createdAt).toLocaleString()}</div>
                                </div>
                            </a>
                        </div>
                    `;
                    } else {
                        notificationItem = `
                    <div class="notification-item">
                        <img src="${avatarSrc}" alt="Profile picture" width="40" height="40">
                        <div class="text">
                            <div class="title">${notification.content}</div>
                            <div class="subtitle">${new Date(notification.createdAt).toLocaleString()}</div>
                        </div>
                    </div>
                `;
                    }
                    notificationPanelWrap.append(notificationItem);
                });

            }

        },
        error: function(error) {
            console.error('Error fetching notifications:', error);
        }
    });
}