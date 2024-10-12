// let currentUserId = null;
// let notificationSocket = null;
//
// window.addEventListener('load', function() {
//     const currentUserId = sessionStorage.getItem('myId');
//
//     if (!currentUserId) {
//         console.log("No user ID found in sessionStorage. Cannot connect to WebSocket.");
//         return;
//     }
//
//     // 自動建立 WebSocket 來接收通知
//     notificationSocket = new WebSocket(`ws://localhost:8081/notifications?userId=${currentUserId}`);
//
//     notificationSocket.onmessage = function(event) {
//         const data = JSON.parse(event.data);
//
//         if (data.type === "invitation") {
//             // 當接收到邀請消息時，顯示接受或拒絕邀請的按鈕
//             showInvitation(data.inviter_id, data.invitee_id);
//         } else if (data.type === "accepted") {
//             // 當接收到邀請已被接受的通知時，邀請方進入聊天室
//             console.log(`Your invitation was accepted by user ${data.invitee_id}.`);
//             startChat(currentUserId, data.invitee_id, currentUserId);
//         }
//     };
//
//     notificationSocket.onerror = function(error) {
//         console.error('WebSocket error:', error);
//     };
//
//     notificationSocket.onopen = function() {
//         console.log('Notification WebSocket connected for user ID: ' + currentUserId);
//         document.getElementById('result').innerText = 'Connected to notifications for user ID: ' + currentUserId;
//     };
// });
// Function to display the invitation with Accept/Decline buttons for the invitee
// document.getElementById('sendInviteBtn').addEventListener('click', function() {
//     const inviteeId = document.getElementById('inviteeId').value;
//
//     if (!currentUserId || !inviteeId) {
//         alert("Please make sure both your user ID and invitee ID are entered.");
//         return;
//     }
//
//     // 發送邀請 API 請求
//     fetch('/chat/invite', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         body: JSON.stringify({
//             inviter_id: currentUserId,  // 發送者的 ID
//             invitee_id: inviteeId  // 接收邀請者的 ID
//         })
//     })
//         .then(response => response.json())
//         .then(data => {
//             console.log(JSON.stringify(data));
//             document.getElementById('result').innerText = `Invitation sent: ${JSON.stringify(data)}`;
//         })
//         .catch(error => {
//             console.error('Error sending invitation:', error);
//         });
// });
//
// // 顯示邀請按鈕的邏輯
// function showInvitation(inviterId, inviteeId) {
//     const inviteMessage = document.createElement('div');
//     inviteMessage.innerHTML = `
//             <p>You have a new chat invitation from User ${inviterId}. Do you want to accept?</p>
//             <button id="acceptBtn">Accept</button>
//             <button id="declineBtn">Decline</button>
//         `;
//
//     document.getElementById('header-placeholder').appendChild(inviteMessage);
//
//     // document.getElementById('invitation').appendChild(inviteMessage);
//
//     // 處理接受邀請的邏輯
//     document.getElementById('acceptBtn').addEventListener('click', function() {
//         acceptInvitation(inviterId, inviteeId);
//     });
//
//     // 處理拒絕邀請的邏輯
//     document.getElementById('declineBtn').addEventListener('click', function() {
//         declineInvitation(inviterId, inviteeId);
//     });
// }
//
// // Accept invitation
// function acceptInvitation(inviterId, inviteeId) {
//     fetch(`/chat/accept`, {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         body: JSON.stringify({ inviter_id: inviterId, invitee_id: inviteeId })
//     })
//         .then(response => response.text())
//         .then(data => {
//             console.log(data);
//             startChat(inviterId, inviteeId, currentUserId);
//         })
//         .catch(error => {
//             console.error('Error accepting invitation:', error);
//         });
// }
//
// // Decline invitation
// function declineInvitation(inviterId, inviteeId) {
//     fetch(`/chat/decline`, {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         body: JSON.stringify({ inviterId: inviterId, inviteeId: inviteeId })
//     })
//         .then(response => response.text())
//         .then(data => {
//             console.log(data);
//             inviteMessage.remove();
//         })
//         .catch(error => {
//             console.error('Error declining invitation:', error);
//         });
// }
//
// // 假設這段程式碼在接受邀請後運行
// function startChat(inviterId, inviteeId, currentUserId) {
//     // 根據邀請人和被邀請人的 ID 排序，生成共享的房間名稱
//     const sortedIds = [inviterId, inviteeId].sort();
//     const roomName = `${sortedIds[0]}_${sortedIds[1]}`;  // 雙方共享的房間名稱
//
//     // 用戶端通過 roomName 進入聊天 WebSocket
//     // const socket = new WebSocket(`wss://wendystylish.online/chat?userId=${currentUserId}&roomName=${roomName}`);
//     const socket = new WebSocket(`ws://localhost:8080/chat?userId=${currentUserId}&roomName=${roomName}`);
//
//     // 處理 WebSocket 連線成功事件
//     socket.onopen = function() {
//         console.log('WebSocket connection established');
//         document.getElementById('messages').innerText = 'Connected to chat room: ' + roomName;
//     };
//
//     // 處理接收到的訊息
//     socket.onmessage = function(event) {
//         const messageDiv = document.createElement('div');
//         messageDiv.innerText = event.data;
//         document.getElementById('messages').appendChild(messageDiv);
//     };
//
//     // 處理 WebSocket 錯誤
//     socket.onerror = function(error) {
//         console.error('WebSocket error:', error);
//     };
//
//     // 發送訊息的邏輯
//     document.getElementById('sendMessageBtn').addEventListener('click', function() {
//         const message = document.getElementById('messageInput').value;
//         if (message) {
//             socket.send(message);  // 發送訊息到 WebSocket 伺服器
//             const messageDiv = document.createElement('div');
//             messageDiv.innerText = message;
//             document.getElementById('messages').appendChild(messageDiv);  // 顯示已發送的訊息
//             document.getElementById('messageInput').value = '';  // 清空輸入框
//         }
//     });
// }