const express = require('express');
const path = require('path');
const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Serve static files from www directory
app.use(express.static(path.join(__dirname, 'www')));

// Health check endpoint
app.get('/api/health', (req, res) => {
  res.json({ status: 'ok', service: 'CompareCart API Server', timestamp: new Date().toISOString() });
});

// Cuelinks affiliate link conversion endpoint
app.post('/api/cuelinks/convert', (req, res) => {
  const { url, merchant } = req.body;
  if (!url) {
    return res.status(400).json({ error: 'URL is required' });
  }
  const publisherId = process.env.CUELINKS_PUBLISHER_ID || '108920';
  const encodedUrl = encodeURIComponent(url);
  const affiliateUrl = `https://linksredirect.com/?cid=${publisherId}&source=comparecart_server&url=${encodedUrl}`;
  
  res.json({
    originalUrl: url,
    affiliateUrl: affiliateUrl,
    merchant: merchant || 'E-Commerce',
    isMonetized: true,
    timestamp: new Date().toISOString()
  });
});

// Fallback to index.html for SPA routing
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'www', 'index.html'));
});

app.listen(PORT, () => {
  console.log(`CompareCart API server running on port ${PORT}`);
});
