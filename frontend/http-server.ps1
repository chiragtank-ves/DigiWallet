$listener = New-Object System.Net.HttpListener
$listener.Prefixes.Add("http://localhost:3000/")
$listener.Start()

Write-Host "Frontend running on http://localhost:3000"

while ($listener.IsListening) {
    $context = $listener.GetContext()
    $localPath = $context.Request.Url.LocalPath.TrimStart('/')
    $filePath = Join-Path (Get-Location) $localPath

    if (-Not (Test-Path $filePath)) {
        $context.Response.StatusCode = 404
        $context.Response.Close()
        continue
    }

    $bytes = [System.IO.File]::ReadAllBytes($filePath)
    $context.Response.ContentLength64 = $bytes.Length
    $context.Response.OutputStream.Write($bytes, 0, $bytes.Length)
    $context.Response.Close()
}
