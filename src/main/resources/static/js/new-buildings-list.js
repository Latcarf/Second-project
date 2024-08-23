document.addEventListener("DOMContentLoaded", function () {
    const typeSelect = document.getElementById('type');
    const newBuildingSection = document.getElementById('newBuildingSection');
    const searchInput = document.getElementById('newBuildingSearch');
    const suggestionsList = document.getElementById('newBuildingSuggestions');
    const newBuildingIdInput = document.getElementById('newBuildingId');
    const numFloorsInput = document.getElementById('numFloors');
    const cityInput = document.getElementById('city');
    const districtInput = document.getElementById('district');

    function initialize() {
        toggleNewBuildingSection();
        if (typeSelect.value === 'APARTMENT') {
            fetchBuildingDetails(newBuildingIdInput.value);
        }
    }

    function toggleNewBuildingSection() {
        if (typeSelect.value === 'APARTMENT') {
            newBuildingSection.style.display = 'block';
            numFloorsInput.readOnly = true;
            cityInput.readOnly = true;
            districtInput.readOnly = true;
        } else {
            newBuildingSection.style.display = 'none';
            numFloorsInput.readOnly = false;
            cityInput.readOnly = false;
            districtInput.readOnly = false;
            clearNewBuildingFields();
        }
    }

    function clearNewBuildingFields() {
        newBuildingIdInput.value = '';
        searchInput.value = '';
        suggestionsList.innerHTML = '';
        numFloorsInput.value = '';
        cityInput.value = '';
        districtInput.value = '';
    }

    function searchNewBuildings(query) {
        if (query.length >= 3) {
            fetch(`/admin/main/new-buildings/search?query=${query}`)
                .then(response => response.json())
                .then(data => displaySuggestions(data))
                .catch(error => console.error('Error fetching buildings:', error));
        } else {
            suggestionsList.innerHTML = '';
        }
    }

    function displaySuggestions(data) {
        suggestionsList.innerHTML = '';
        data.forEach(building => {
            const listItem = document.createElement('li');
            listItem.textContent = building.name;
            listItem.style.cursor = 'pointer';
            listItem.addEventListener('click', function () {
                searchInput.value = building.name;
                newBuildingIdInput.value = building.id;
                fetchBuildingDetails(building.id);
                suggestionsList.innerHTML = '';
            });
            suggestionsList.appendChild(listItem);
        });
    }

    function fetchBuildingDetails(newBuildingId) {
        if (newBuildingId) {
            fetch(`/admin/main/new-buildings/get-num-floors?newBuildingId=${newBuildingId}`)
                .then(response => response.json())
                .then(data => {
                    numFloorsInput.value = data;
                })
                .catch(error => console.error('Error fetching num floors:', error));

            fetch(`/admin/main/new-buildings/get-address?newBuildingId=${newBuildingId}`)
                .then(response => response.json())
                .then(data => {
                    cityInput.value = data.city;
                    districtInput.value = data.district;
                })
                .catch(error => console.error('Error fetching address:', error));
        }
    }

    typeSelect.addEventListener('change', toggleNewBuildingSection);
    searchInput.addEventListener('input', function () {
        searchNewBuildings(searchInput.value);
    });

    initialize();
});