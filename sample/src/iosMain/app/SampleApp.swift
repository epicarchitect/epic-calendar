//
//  appApp.swift
//  app
//
//  Created by alex on 24.09.2024.
//

import SwiftUI
import KMPLib

@main
struct SampleApp: App {
    var body: some Scene {
        WindowGroup {
            ComposeView()
        }
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainKt.MainViewController()
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
