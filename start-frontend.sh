#!/bin/bash

echo "⚛️ Starting Parking Garage Frontend..."
cd frontend

# Check if node_modules exists
if [ ! -d "node_modules" ]; then
    echo "📦 Installing dependencies..."
    npm install
fi

npm run dev
