let steps = [], currentStep = 0, isPlaying = false, playInterval;

async function loadArray() {
    const algo = document.getElementById('algoSelect').value.toLowerCase().replace(' ', '');
    const arr = document.getElementById('arrayInput').value;

    if (!arr) {
        alert("Array daal bhai");
        return;
    }

    try {
        const res = await fetch(`/sort/${algo}?arr=${arr}`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        steps = await res.json();
        currentStep = 0;
        updateVisualization();
        document.getElementById('message').textContent = `Loaded ${steps.length} steps. Click Play!`;
    } catch (err) {
        document.getElementById('message').textContent = "Error: Backend chalu hai?";
        console.error(err);
    }
}

function updateVisualization() {
    if (currentStep >= steps.length) {
        stopAnimation();
        return;
    }

    const step = steps[currentStep];
    document.getElementById('message').textContent = step.message;
    document.getElementById('stepCounter').textContent = `Step: ${currentStep + 1} / ${steps.length}`;

    const container = document.getElementById('bars');
    container.innerHTML = '';

    const maxVal = Math.max(...step.array);

    step.array.forEach((val, idx) => {
        const bar = document.createElement('div');
        bar.className = 'bar';
        bar.style.height = `${(val / maxVal) * 100}%`;
        bar.textContent = val;

        // Color logic - ye important hai
        if (idx === step.indexI) bar.classList.add('active');
        if (idx === step.indexJ) bar.classList.add('compare');
        if (step.swapped && (idx === step.indexI || idx === step.indexJ)) {
            bar.classList.add('swap');
        }
        if (step.sorted && step.sorted.includes(idx)) {
            bar.classList.add('sorted');
        }

        container.appendChild(bar);
    });
}

function playAnimation() {
    if (isPlaying || steps.length === 0) return;
    isPlaying = true;
    const speed = 11 - document.getElementById('speed').value;
    playInterval = setInterval(() => {
        if (currentStep < steps.length - 1) {
            currentStep++;
            updateVisualization();
        } else {
            stopAnimation();
        }
    }, speed * 100);
}

function stopAnimation() {
    isPlaying = false;
    clearInterval(playInterval);
}

function nextStep() {
    stopAnimation();
    if (currentStep < steps.length - 1) {
        currentStep++;
        updateVisualization();
    }
}

function prevStep() {
    stopAnimation();
    if (currentStep > 0) {
        currentStep--;
        updateVisualization();
    }
}

function resetArray() {
    stopAnimation();
    currentStep = 0;
    if (steps.length > 0) updateVisualization();
}

document.getElementById('loadBtn').onclick = loadArray;
document.getElementById('playBtn').onclick = playAnimation;
document.getElementById('pauseBtn').onclick = stopAnimation;
document.getElementById('nextBtn').onclick = nextStep;
document.getElementById('prevBtn').onclick = prevStep;
document.getElementById('resetBtn').onclick = resetArray;