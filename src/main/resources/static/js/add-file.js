const fileInputs = document.querySelectorAll('.custom-file-upload input[type="file"]');
fileInputs.forEach((input, index) => {
    const fileChosen = document.getElementById(`file-chosen${index + 1}`);
    input.addEventListener('change', function() {
        fileChosen.textContent = this.files.length > 0 ? this.files[0].name : 'No file chosen';
    });
});

const initialBannerSrc = document.getElementById('banner-preview').src;

function previewBanner(event) {
    const [file] = event.target.files;
    const preview = document.getElementById('banner-preview');

    if (file) {
        preview.src = URL.createObjectURL(file);
        preview.onload = () => URL.revokeObjectURL(preview.src);
    } else {
        preview.src = initialBannerSrc;
    }
}

document.getElementById('banner').addEventListener('change', function() {
    if (!this.files.length) {
        document.getElementById('banner-preview').src = initialBannerSrc;
    }
});