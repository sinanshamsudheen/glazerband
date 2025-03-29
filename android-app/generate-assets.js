const fs = require('fs');
const { createCanvas } = require('canvas');

// Create a 1024x1024 canvas
const canvas = createCanvas(1024, 1024);
const ctx = canvas.getContext('2d');

// Fill background
ctx.fillStyle = '#ffffff';
ctx.fillRect(0, 0, 1024, 1024);

// Draw text
ctx.fillStyle = '#000000';
ctx.font = 'bold 48px Arial';
ctx.textAlign = 'center';
ctx.textBaseline = 'middle';
ctx.fillText('BandApp', 512, 512);

// Save as PNG
const buffer = canvas.toBuffer('image/png');

// Save files
fs.writeFileSync('./assets/icon.png', buffer);
fs.writeFileSync('./assets/splash.png', buffer);
fs.writeFileSync('./assets/adaptive-icon.png', buffer);
fs.writeFileSync('./assets/favicon.png', buffer); 