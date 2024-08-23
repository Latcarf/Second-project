const fileInputs = document.querySelectorAll('.custom-file-upload input[type="file"]');
fileInputs.forEach((input, index) => {
    const fileChosen = document.getElementById(`file-chosen${index + 1}`);
    input.addEventListener('change', function() {
        fileChosen.textContent = this.files.length > 0 ? this.files[0].name : 'No file chosen';
    });
});