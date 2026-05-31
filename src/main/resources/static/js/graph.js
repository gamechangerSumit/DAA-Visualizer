const svg = d3.select("#graph");
const width = document.getElementById('graph').clientWidth;
const height = document.getElementById('graph').clientHeight;
svg.attr("width", width).attr("height", height);

let nodes = [], links = [], steps = [];
let currentStep = 0, isPlaying = false, playInterval;

const simulation = d3.forceSimulation()
   .force("link", d3.forceLink().id(d => d.id).distance(120))
   .force("charge", d3.forceManyBody().strength(-400))
   .force("center", d3.forceCenter(width / 2, height / 2));

function drawGraph(edgesStr) {
    // Parse edges "0-1,0-2" -> nodes + links
    const edgeList = edgesStr.split(',').map(e => e.trim().split('-').map(Number));
    const nodeSet = new Set();
    edgeList.forEach(([u, v]) => { nodeSet.add(u); nodeSet.add(v); });

    nodes = Array.from(nodeSet).map(id => ({ id, visited: false }));
    links = edgeList.map(([source, target]) => ({ source, target }));

    svg.selectAll("*").remove();

    const link = svg.append("g")
       .selectAll("line")
       .data(links)
       .join("line")
       .attr("class", "link");

    const node = svg.append("g")
       .selectAll("g")
       .data(nodes)
       .join("g")
       .attr("class", "node unvisited")
       .call(d3.drag()
           .on("start", dragstarted)
           .on("drag", dragged)
           .on("end", dragended));

    node.append("circle").attr("r", 25);
    node.append("text")
       .attr("dy", 5)
       .attr("text-anchor", "middle")
       .text(d => d.id);

    simulation.nodes(nodes).on("tick", ticked);
    simulation.force("link").links(links);
    simulation.alpha(1).restart();

    function ticked() {
        link.attr("x1", d => d.source.x)
           .attr("y1", d => d.source.y)
           .attr("x2", d => d.target.x)
           .attr("y2", d => d.target.y);
        node.attr("transform", d => `translate(${d.x},${d.y})`);
    }
}

function dragstarted(event, d) {
    if (!event.active) simulation.alphaTarget(0.3).restart();
    d.fx = d.x; d.fy = d.y;
}
function dragged(event, d) {
    d.fx = event.x; d.fy = event.y;
}
function dragended(event, d) {
    if (!event.active) simulation.alphaTarget(0);
    d.fx = null; d.fy = null;
}

async function loadGraph() {
    const algo = document.getElementById('algoSelect').value;
    const edges = document.getElementById('edgesInput').value;
    const start = document.getElementById('startNode').value;

    if (!edges) return alert("Edges daal bhai");

    drawGraph(edges);

    try {
        const res = await fetch(`/graph/${algo}?start=${start}&edges=${edges}`);
        steps = await res.json();
        currentStep = 0;
        updateVisualization();
        document.getElementById('message').textContent = `Graph loaded. ${steps.length} steps. Click Play or Next.`;
    } catch (err) {
        alert("Backend error: " + err);
    }
}

function updateVisualization() {
    if (currentStep >= steps.length) {
        stopAnimation();
        return;
    }

    const step = steps[currentStep];
    document.getElementById('message').textContent = `Step ${currentStep + 1}/${steps.length}: ${step.message}`;

    // Reset all
    svg.selectAll(".node").attr("class", "node unvisited");
    svg.selectAll(".link").attr("class", "link");

    // Mark visited nodes
    step.visitedNodes.forEach(id => {
        svg.selectAll(".node").filter(d => d.id === id)
           .attr("class", "node visited");
    });

    // Mark current node
    if (step.currentNode!== -1) {
        svg.selectAll(".node").filter(d => d.id === step.currentNode)
           .attr("class", "node current");
    }

    // Highlight edges
    step.edges.forEach(([u, v]) => {
        svg.selectAll(".link").filter(d =>
            (d.source.id === u && d.target.id === v) || (d.source.id === v && d.target.id === u)
        ).attr("class", "link active");
    });
}

function playAnimation() {
    if (isPlaying) return;
    isPlaying = true;
    const speed = 11 - document.getElementById('speed').value;
    playInterval = setInterval(() => {
        if (currentStep < steps.length - 1) {
            currentStep++;
            updateVisualization();
        } else {
            stopAnimation();
        }
    }, speed * 200);
}

function stopAnimation() {
    isPlaying = false;
    clearInterval(playInterval);
}

function nextStep() {
    if (currentStep < steps.length - 1) {
        currentStep++;
        updateVisualization();
    }
}

function resetGraph() {
    stopAnimation();
    currentStep = 0;
    if (steps.length > 0) updateVisualization();
}

// Event listeners
document.getElementById('loadBtn').onclick = loadGraph;
document.getElementById('playBtn').onclick = playAnimation;
document.getElementById('pauseBtn').onclick = stopAnimation;
document.getElementById('nextBtn').onclick = nextStep;
document.getElementById('resetBtn').onclick = resetGraph;

// Auto load on page open
loadGraph();