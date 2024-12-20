let currentIndex = 1;
let totalImages = 1; // Start with 1 for the add-image-frame

function showImage(index) {
    const images = document.querySelectorAll('.carousel-images img, .add-image-frame');
    const dots = document.querySelectorAll('.carousel-dots .dot');

    images.forEach((img, i) => {
        img.style.opacity = i === index ? '1' : '0';
    });
    dots.forEach((dot, i) => {
        dot.classList.toggle('active', i === index);
    });

    const imageOverlay = document.getElementById('image-overlay');
    if(imageOverlay !== null) {
        if (index === totalImages - 1 || totalImages === 1) {
            imageOverlay.style.display = 'none';
        } else {
            imageOverlay.style.display = 'flex';
        }
    }
}

function nextImage() {
    currentIndex = (currentIndex + 1) % totalImages;
    showImage(currentIndex);
}

function prevImage() {
    currentIndex = (currentIndex - 1 + totalImages) % totalImages;
    showImage(currentIndex);
}

function goToImage(index) {
    currentIndex = index;
    showImage(currentIndex);
}

function addImage(event) {
    const files = event.target.files;
    const carouselImages = document.querySelector('.carousel-images');
    const carouselDots = document.querySelector('.carousel-dots');

    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const reader = new FileReader();

        reader.onload = function(e) {
            const newImage = document.createElement('img');
            newImage.src = e.target.result;
            newImage.alt = 'Newly added image';
            newImage.style.opacity = '0';
            newImage.onload = function() {
                newImage.style.opacity = '1';
            };
            carouselImages.insertBefore(newImage, document.querySelector('.add-image-frame'));

            const newDot = document.createElement('div');
            newDot.classList.add('dot');
            newDot.onclick = () => goToImage(totalImages - 1);
            carouselDots.insertBefore(newDot, carouselDots.lastChild);

            totalImages++;
            currentIndex = totalImages - 2;
            showImage(currentIndex);
        };

        reader.readAsDataURL(file);
    }
}

function editImage() {
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = 'image/*';
    fileInput.onchange = function(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const images = document.querySelectorAll('.carousel-images img');
                images[currentIndex].src = e.target.result;
            };
            reader.readAsDataURL(file);
        }
    };
    fileInput.click();
}

function closePopup() {
    document.querySelector('.post-creation').style.display = 'none';
    document.getElementById('popup-container').style.display = 'none';
    document.body.style.backgroundColor = 'white';
}

showImage(currentIndex);

function sharePost() {
    const content = document.querySelector('textarea').value;
    const privacy = document.querySelector('select').value;
    const images = document.querySelectorAll('.carousel-images img:not(.add-image-frame)');
    const imagesData = [];

    images.forEach(img => {
        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d');
        canvas.width = img.naturalWidth;
        canvas.height = img.naturalHeight;
        ctx.drawImage(img, 0, 0);
        imagesData.push(canvas.toDataURL('image/png').split(',')[1]);
    });

    $.ajax({
        url: '/api/posts/create',
        type: 'POST',
        contentType: 'application/json', // Send data as JSON
        data: JSON.stringify({
            content: content,
            privacy: privacy,
            imagesData: imagesData // Base64 strings
        }),
        success: function(response) {
            console.log(response);
            alert('Post created successfully!');
            closePopup();
        },
        error: function(xhr, status, error) {
            alert('Failed to create post.');
        }
    });
}