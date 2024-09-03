document.addEventListener('DOMContentLoaded', function () {
    const fileInputs = document.querySelectorAll('.custom-file-upload input[type="file"]');
    const initialPreviewSources = {};

    fileInputs.forEach((input, index) => {
        const previewId = input.id + '-preview';
        const fileChosenId = 'file-chosen' + (index + 1);

        const previewElement = document.getElementById(previewId);
        const fileChosenElement = document.getElementById(fileChosenId);

        if (previewElement) {
            initialPreviewSources[previewId] = previewElement.src;

            input.addEventListener('change', function () {
                if (this.files.length > 0) {
                    fileChosenElement.textContent = this.files[0].name;
                    previewElement.src = URL.createObjectURL(this.files[0]);
                    previewElement.onload = () => URL.revokeObjectURL(previewElement.src);
                } else {
                    fileChosenElement.textContent = 'No file chosen';
                    previewElement.src = initialPreviewSources[previewId];
                }
            });
        }
    });
});
