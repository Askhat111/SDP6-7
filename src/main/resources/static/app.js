class WeatherApp {
    constructor() {
        this.API_BASE = '/api';
        this.availableCities = ['Astana', 'Karaganda', 'Almaty'];
        this.demoUsers = [
            {
                name: 'Asyl',
                email: 'asyl@email.com',
                cities: ['Astana', 'Almaty'],
                observers: ['mobile', 'email']
            },
            {
                name: 'Bekzat',
                email: 'bekzat@email.com',
                cities: ['Karaganda'],
                observers: ['display', 'mobile']
            }
        ];
        this.weatherHistory = [];
        this.notificationHistory = [];
        this.lastTemperatures = {};
        this.weatherPatterns = {};
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.loadDemoData();
        this.showToast('Weather System Started!', 'success');
    }

    setupEventListeners() {
        document.getElementById('userCity').addEventListener('change', (e) => {
            this.handleCustomCitySelection(e.target.value);
        });

        document.getElementById('strategySelect').addEventListener('change', (e) => {
            this.handleStrategyChange(e.target.value);
        });

        setInterval(() => {
            this.updateAllCities();
        }, 60000);
    }

    handleCustomCitySelection(selectedValue) {
        const customContainer = document.getElementById('customCityContainer');
        customContainer.style.display = selectedValue === 'custom' ? 'block' : 'none';
    }

    handleStrategyChange(strategy) {
        const manualContainer = document.getElementById('manualTempContainer');
        manualContainer.style.display = strategy === 'manual' ? 'block' : 'none';
    }

    loadDemoData() {
        this.updateCitiesDisplay();
        this.addDemoWeatherData();
    }

    updateCitiesDisplay() {
        const citiesContainer = document.getElementById('citiesContainer');
        const citySelect = document.getElementById('citySelect');
        const citiesCount = document.getElementById('citiesCount');

        const allCities = [...new Set(this.demoUsers.flatMap(user => user.cities))];

        citiesContainer.innerHTML = allCities.map(city =>
            `<span class="badge bg-primary city-badge me-2 mb-2 p-2">
                <i class="fas fa-location-dot me-1"></i>${city}
            </span>`
        ).join('');

        citySelect.innerHTML = '<option value="">Select a city...</option>' +
            allCities.map(city => `<option value="${city}">${city}</option>`).join('');

        document.getElementById('updateCityBtn').disabled = false;
        citiesCount.textContent = allCities.length;
    }

    updateCityDropdowns() {
        const userCitySelect = document.getElementById('userCity');
        const citySelect = document.getElementById('citySelect');

        const customOption = userCitySelect.querySelector('option[value="custom"]');
        userCitySelect.innerHTML = '';
        this.availableCities.forEach(city => {
            userCitySelect.appendChild(new Option(city, city));
        });
        userCitySelect.appendChild(customOption);

        citySelect.innerHTML = '<option value="">Select a city...</option>' +
            this.availableCities.map(city => `<option value="${city}">${city}</option>`).join('');
    }

    async changeStrategy() {
        const strategy = document.getElementById('strategySelect').value;
        const strategyNames = {
            'realtime': 'Real-Time API',
            'forecast': '3-Day Forecast',
            'manual': 'Manual Input'
        };

        try {
            const response = await fetch(`${this.API_BASE}/change-strategy?strategy=${strategy}`, {
                method: 'POST'
            });

            if (response.ok) {
                this.showToast(`Strategy changed to: ${strategyNames[strategy]}`, 'success');
            } else {
                throw new Error('Failed to change strategy');
            }
        } catch (error) {
            console.error('Error changing strategy:', error);
            this.showToast('Error changing strategy', 'error');
        }
    }

    async updateAllCities() {
        try {
            this.setLoading(true);
            const response = await fetch(`${this.API_BASE}/update-all`, {
                method: 'POST'
            });

            if (response.ok) {
                this.showToast('All cities updated successfully!', 'success');
                this.demoUsers.forEach(user => {
                    user.cities.forEach(city => {
                        this.addWeatherToHistory(city);
                    });
                });
            } else {
                throw new Error('Failed to update cities');
            }
        } catch (error) {
            console.error('Error updating cities:', error);
            this.showToast('Error updating cities', 'error');
        } finally {
            this.setLoading(false);
        }
    }

    async updateCity() {
        const city = document.getElementById('citySelect').value;
        if (!city) {
            this.showToast('Please select a city', 'error');
            return;
        }

        try {
            this.setLoading(true);
            const response = await fetch(`${this.API_BASE}/update-weather?city=${encodeURIComponent(city)}`, {
                method: 'POST'
            });

            if (response.ok) {
                this.showToast(`Weather updated for: ${city}`, 'success');
                this.addWeatherToHistory(city);
            } else {
                throw new Error('Failed to update city');
            }
        } catch (error) {
            console.error('Error updating city:', error);
            this.showToast('Error updating city', 'error');
        } finally {
            this.setLoading(false);
        }
    }

    async addUser() {
        const name = document.getElementById('userName').value;
        const email = document.getElementById('userEmail').value;
        const citySelect = document.getElementById('userCity');
        const selectedCity = citySelect.value;
        const customCity = document.getElementById('customCity').value;

        let city;
        if (selectedCity === 'custom') {
            if (!customCity) {
                this.showToast('Please enter a custom city name', 'error');
                return;
            }
            city = customCity.trim();
            if (!this.availableCities.includes(city)) {
                this.availableCities.push(city);
                this.updateCityDropdowns();
            }
        } else {
            city = selectedCity;
        }

        if (!name || !email || !city) {
            this.showToast('Please fill all fields', 'error');
            return;
        }

        try {
            const response = await fetch(`${this.API_BASE}/add-user`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `name=${encodeURIComponent(name)}&email=${encodeURIComponent(email)}&city=${encodeURIComponent(city)}`
            });

            if (response.ok) {
                this.showToast(`User ${name} subscribed to ${city}!`, 'success');
                const existingUser = this.demoUsers.find(user => user.name === name);
                if (existingUser) {
                    if (!existingUser.cities.includes(city)) {
                        existingUser.cities.push(city);
                    }
                } else {
                    this.demoUsers.push({
                        name,
                        email,
                        cities: [city],
                        observers: ['mobile', 'email', 'display']
                    });
                }
                this.clearUserForm();
                this.updateCitiesDisplay();
            } else {
                throw new Error('Failed to add user');
            }
        } catch (error) {
            console.error('Error adding user:', error);
            this.showToast('Error adding user', 'error');
        }
    }

    clearUserForm() {
        document.getElementById('userName').value = '';
        document.getElementById('userEmail').value = '';
        document.getElementById('customCity').value = '';
        document.getElementById('customCityContainer').style.display = 'none';
        document.getElementById('userCity').value = 'Astana';
    }

    addWeatherToHistory(city) {
        const strategy = document.getElementById('strategySelect').value;
        const weatherData = this.generateWeatherData(city, strategy);

        this.weatherHistory.unshift(weatherData);

        if (this.weatherHistory.length > 10) {
            this.weatherHistory = this.weatherHistory.slice(0, 10);
        }

        this.notifyObservers(city, weatherData);

        this.updateWeatherDisplay();
    }

    notifyObservers(city, weatherData) {
        const notifications = [];


        const subscribedUsers = this.demoUsers.filter(user =>
            user.cities.includes(city)
        );

        subscribedUsers.forEach(user => {
            user.observers.forEach(observerType => {
                const notification = this.createObserverNotification(user, observerType, weatherData);
                notifications.push(notification);
                this.showObserverNotification(notification);
            });
        });

        return notifications;
    }

    createObserverNotification(user, observerType, weatherData) {
        const timestamp = new Date().toLocaleTimeString();
        let message = '';
        let icon = '';

        switch(observerType) {
            case 'mobile':
                icon = '📱';
                message = `${icon} Mobile App for ${user.name}: ${weatherData.condition} in ${weatherData.city} (${weatherData.temperature}°C)`;
                break;
            case 'email':
                icon = '📧';
                message = `${icon} Email to ${user.email}: Weather alert - ${weatherData.condition} in ${weatherData.city}`;
                break;
            case 'display':
                icon = '🖥️';
                message = `${icon} Display for ${user.name}: ${weatherData.city} - ${weatherData.temperature}°C, ${weatherData.humidity}% humidity, ${weatherData.windSpeed} km/h wind`;
                break;
        }

        return {
            user: user.name,
            type: observerType,
            message: message,
            icon: icon,
            timestamp: timestamp,
            city: weatherData.city,
            weather: weatherData
        };
    }

    showObserverNotification(notification) {

        this.notificationHistory.unshift(notification);
        if (this.notificationHistory.length > 20) {
            this.notificationHistory = this.notificationHistory.slice(0, 20);
        }


        this.updateNotificationsDisplay();


        if (notification.type === 'mobile' || this.isSevereWeather(notification.weather)) {
            this.showToast(notification.message, 'info');
        }
    }

    isSevereWeather(weather) {
        return weather.condition.includes('Snowy') ||
            weather.condition.includes('Rainy') && weather.temperature < 5 ||
            weather.windSpeed > 30;
    }

    updateNotificationsDisplay() {
        const notificationsContainer = document.getElementById('notificationsContainer');
        const notificationsCount = document.getElementById('notificationsCount');

        if (!notificationsContainer) return;

        if (this.notificationHistory.length === 0) {
            notificationsContainer.innerHTML = '<div class="text-muted text-center">No notifications yet</div>';
            notificationsCount.textContent = '0';
            return;
        }

        notificationsContainer.innerHTML = this.notificationHistory.map(notif => `
            <div class="alert alert-light border ${this.getNotificationBorderClass(notif.type)} mb-2">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <strong>${notif.icon} ${notif.type.toUpperCase()}</strong>
                        <small class="text-muted ms-2">for ${notif.user}</small>
                        <div class="mt-1">${notif.message}</div>
                    </div>
                    <small class="text-muted">${notif.timestamp}</small>
                </div>
            </div>
        `).join('');

        notificationsCount.textContent = this.notificationHistory.length;
    }

    getNotificationBorderClass(observerType) {
        switch(observerType) {
            case 'mobile': return 'border-primary';
            case 'email': return 'border-success';
            case 'display': return 'border-info';
            default: return 'border-secondary';
        }
    }

    generateWeatherData(city, strategy) {
        let condition, temp, humidity, windSpeed, history, displayCity = city;


        if (!this.weatherPatterns[city]) {
            this.weatherPatterns[city] = {
                baseTemp: this.getCityBaseTemp(city),
                trend: Math.random() > 0.5 ? 'rising' : 'falling',
                variability: Math.random() * 10 + 5
            };
        }

        const pattern = this.weatherPatterns[city];

        if (strategy === 'forecast') {

            condition = this.getForecastCondition(city);
            temp = this.getForecastTemperature(city, pattern);
            humidity = this.getStableHumidity(city);
            windSpeed = this.getStableWindSpeed(city);
            history = this.generateForecastHistory(temp, pattern);
        } else if (strategy === 'manual') {

            condition = "Manual Entry";
            temp = this.getManualTemperature();
            humidity = 50;
            windSpeed = 5;
            history = [19, 20, 21];
            displayCity = city + " (Manual)";
        } else {

            condition = this.getRealisticCondition(city, pattern);
            temp = this.getRealisticTemperature(city, pattern);
            humidity = this.getRealisticHumidity(condition);
            windSpeed = this.getRealisticWindSpeed(condition);
            history = this.generateRealisticHistory(temp, pattern);
        }


        this.lastTemperatures[city] = temp;

        this.updateWeatherPattern(pattern);

        return this.createWeatherObject(city, displayCity, condition, temp, humidity, windSpeed, history, strategy);
    }

    getRealisticTemperature(city, pattern) {
        const baseTemp = pattern.baseTemp;
        const hour = new Date().getHours();


        const dailyVariation = Math.sin((hour - 6) * Math.PI / 12) * 8;

        const randomVariation = (Math.random() - 0.5) * pattern.variability;

        const trendEffect = pattern.trend === 'rising' ? 2 : -2;

        let newTemp = baseTemp + dailyVariation + randomVariation + trendEffect;


        if (this.lastTemperatures[city] !== undefined) {
            const lastTemp = this.lastTemperatures[city];
            const maxChange = 5; // Maximum 5°C change per update
            newTemp = Math.max(lastTemp - maxChange, Math.min(lastTemp + maxChange, newTemp));
        }

        return Math.round(newTemp);
    }

    getForecastTemperature(city, pattern) {

        const baseTemp = pattern.baseTemp;
        const hour = new Date().getHours();
        const dailyVariation = Math.sin((hour - 6) * Math.PI / 12) * 4; // Smaller variation
        const randomVariation = (Math.random() - 0.5) * 3; // Less random

        return Math.round(baseTemp + dailyVariation + randomVariation);
    }

    getManualTemperature() {

        const manualTempInput = document.getElementById('manualTemp');
        return manualTempInput && manualTempInput.value ?
            parseInt(manualTempInput.value) : 20;
    }

    getRealisticCondition(city, pattern) {
        const conditions = {
            'Astana': ['Snowy', 'Cloudy', 'Windy', 'Snowy', 'Cloudy'],
            'Karaganda': ['Cloudy', 'Sunny', 'Windy', 'Cloudy', 'Sunny'],
            'Almaty': ['Sunny', 'Cloudy', 'Rainy', 'Sunny', 'Cloudy']
        };

        const cityConditions = conditions[city] || ['Sunny', 'Cloudy', 'Rainy'];
        let weights = Array(cityConditions.length).fill(1);

        const temp = this.lastTemperatures[city] || pattern.baseTemp;

        if (temp < 0) weights = [3, 2, 1, 3, 2];
        if (temp > 20) weights = [1, 2, 3, 1, 2];


        const totalWeight = weights.reduce((a, b) => a + b, 0);
        let random = Math.random() * totalWeight;

        for (let i = 0; i < cityConditions.length; i++) {
            random -= weights[i];
            if (random <= 0) return cityConditions[i];
        }

        return cityConditions[0];
    }

    getForecastCondition(city) {
        const forecastConditions = {
            'Astana': 'Partly Cloudy',
            'Karaganda': 'Mostly Sunny',
            'Almaty': 'Clear'
        };
        return forecastConditions[city] || 'Sunny';
    }

    getRealisticHumidity(condition) {
        const baseHumidity = {
            'Sunny': 40, 'Cloudy': 65, 'Rainy': 85, 'Snowy': 75, 'Windy': 50, 'Foggy': 90
        };
        return (baseHumidity[condition] || 60) + Math.floor(Math.random() * 20) - 10;
    }

    getRealisticWindSpeed(condition) {
        const baseWind = {
            'Sunny': 8, 'Cloudy': 12, 'Rainy': 18, 'Snowy': 15, 'Windy': 25, 'Foggy': 5
        };
        return (baseWind[condition] || 10) + Math.floor(Math.random() * 8) - 4;
    }

    generateRealisticHistory(currentTemp, pattern) {
        const history = [currentTemp];
        let lastTemp = currentTemp;

        for (let i = 1; i < 6; i++) {

            const change = (Math.random() - 0.5) * 4;
            lastTemp = Math.round(lastTemp + change);
            history.push(lastTemp);
        }

        return history;
    }

    generateForecastHistory(currentTemp, pattern) {

        const trend = pattern.trend === 'rising' ? 1 : -1;
        return [
            currentTemp,
            currentTemp + trend,
            currentTemp + (trend * 2),
            currentTemp + trend,
            currentTemp,
            currentTemp - trend
        ];
    }

    updateWeatherPattern(pattern) {

        if (Math.random() < 0.3) {
            pattern.trend = Math.random() > 0.5 ? 'rising' : 'falling';
        }

        if (Math.random() < 0.2) {
            pattern.baseTemp += (Math.random() - 0.5) * 2;
        }
    }

    createWeatherObject(city, displayCity, condition, temp, humidity, windSpeed, history, strategy) {
        const conditions = {
            'Sunny': { icon: '☀️', class: 'bg-warning text-dark' },
            'Cloudy': { icon: '☁️', class: 'bg-secondary' },
            'Rainy': { icon: '🌧️', class: 'bg-info' },
            'Snowy': { icon: '❄️', class: 'bg-light text-dark' },
            'Windy': { icon: '💨', class: 'bg-primary' },
            'Foggy': { icon: '🌫️', class: 'bg-dark' },
            'Partly Cloudy': { icon: '⛅', class: 'bg-info text-dark' },
            'Mostly Sunny': { icon: '🌤️', class: 'bg-warning text-dark' },
            'Clear': { icon: '☀️', class: 'bg-warning text-dark' },
            'Partly Cloudy (Forecast)': { icon: '⛅', class: 'bg-info text-dark' },
            'Manual Entry': { icon: '✏️', class: 'bg-dark text-light' }
        };

        const conditionInfo = conditions[condition] || conditions['Cloudy'];
        const timestamp = new Date();

        return {
            city: displayCity,
            temperature: temp,
            humidity: humidity,
            windSpeed: windSpeed,
            condition: condition,
            icon: conditionInfo.icon,
            conditionClass: conditionInfo.class,
            tempClass: this.getTempClass(temp),
            windClass: this.getWindClass(windSpeed),
            timestamp: timestamp.toLocaleTimeString(),
            strategy: strategy,
            history: history
        };
    }

    addDemoWeatherData() {
        this.demoUsers.forEach(user => {
            user.cities.forEach(city => {
                this.addWeatherToHistory(city);
            });
        });
    }

    updateWeatherDisplay() {
        const weatherHistoryEl = document.getElementById('weatherHistory');
        const weatherCount = document.getElementById('weatherCount');

        if (this.weatherHistory.length === 0) {
            weatherHistoryEl.innerHTML = '<div class="text-muted text-center">No weather updates yet. Use the controls above to get started!</div>';
            weatherCount.textContent = '0';
            return;
        }

        weatherHistoryEl.innerHTML = this.weatherHistory.map(weather => `
            <div class="card weather-card mb-3 strategy-${weather.strategy}">
                <div class="card-body">
                    <div class="row align-items-center">
                        <div class="col-md-2">
                            <h5 class="card-title">${weather.city}</h5>
                            <div>
                                <span class="weather-icon">${weather.icon}</span>
                                <span class="badge ms-2 ${weather.conditionClass}">${weather.condition}</span>
                            </div>
                            <small class="text-muted">${weather.strategy}</small>
                        </div>
                        <div class="col-md-2 text-center">
                            <strong class="fs-4 ${weather.tempClass}">${weather.temperature}°C</strong>
                        </div>
                        <div class="col-md-2">
                            <div class="mb-1">
                                <i class="fas fa-tint text-info me-1"></i>
                                <strong>Humidity:</strong> ${weather.humidity}%
                            </div>
                            <div class="mb-1">
                                <i class="fas fa-wind text-primary me-1"></i>
                                <strong>Wind:</strong> 
                                <span class="wind-indicator ${weather.windClass}">${weather.windSpeed} km/h</span>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="progress temp-progress mb-2">
                                <div class="progress-bar ${weather.tempClass.replace('text', 'bg')}" 
                                     style="width: ${((weather.temperature + 10) / 45) * 100}%">
                                </div>
                            </div>
                            <small class="text-muted">Temperature scale: -10°C to 35°C</small>
                            <div class="mt-1">
                                <small class="text-muted">History: ${weather.history.join(' → ')}°C</small>
                            </div>
                        </div>
                        <div class="col-md-3 text-end">
                            <small class="text-muted">
                                <i class="fas fa-clock me-1"></i>${weather.timestamp}
                            </small>
                            <br>
                            <small class="text-muted">Source: ${weather.strategy}</small>
                        </div>
                    </div>
                </div>
            </div>
        `).join('');

        weatherCount.textContent = this.weatherHistory.length;
    }

    showToast(message, type = 'info') {
        const toastEl = document.getElementById('liveToast');
        const toastMessage = document.getElementById('toastMessage');
        const toastTime = document.getElementById('toastTime');

        toastMessage.textContent = message;
        toastTime.textContent = new Date().toLocaleTimeString();

        const toastHeader = toastEl.querySelector('.toast-header');
        const headerClass = type === 'success' ? 'bg-success text-white' :
            type === 'error' ? 'bg-danger text-white' : 'bg-primary text-white';
        toastHeader.className = `toast-header ${headerClass}`;

        const toast = new bootstrap.Toast(toastEl);
        toast.show();
    }


    getCityBaseTemp(city) {
        const baseTemps = {
            'Astana': -5,
            'Karaganda': -1,
            'Almaty': 6
        };
        return baseTemps[city] || 10;
    }

    getStableHumidity(city) {
        const baseHumidity = {
            'Astana': 75,
            'Karaganda': 70,
            'Almaty': 65
        };
        return baseHumidity[city] || 60;
    }

    getStableWindSpeed(city) {
        const windSpeeds = {
            'Astana': 15,
            'Karaganda': 12,
            'Almaty': 8
        };
        return windSpeeds[city] || 10;
    }

    getTempClass(temp) {
        if (temp > 25) return 'text-danger';
        if (temp > 15) return 'text-success';
        if (temp > 5) return 'text-info';
        return 'text-primary';
    }

    getWindClass(windSpeed) {
        if (windSpeed > 30) return 'wind-strong';
        if (windSpeed > 20) return 'wind-moderate';
        return 'wind-calm';
    }

    setLoading(loading) {
        if (loading) {
            document.body.classList.add('loading');
        } else {
            document.body.classList.remove('loading');
        }
    }
}


document.addEventListener('DOMContentLoaded', function() {
    window.weatherApp = new WeatherApp();
});

function changeStrategy() { window.weatherApp.changeStrategy(); }
function updateAllCities() { window.weatherApp.updateAllCities(); }
function updateCity() { window.weatherApp.updateCity(); }
function addUser() { window.weatherApp.addUser(); }