use std::error::Error;
use std::io;

fn main() -> Result<(), Box<dyn Error>> {
    let volume: f64 = get_input("Initial volume")?.parse()?;

    if volume <= 0.0 {
        return Err("Volume must be greater than 0.".into());
    }

    let initial_concentration: f64 = get_input("Initial concentration")?.parse()?;

    if initial_concentration < 0.0 || initial_concentration > 1.0 {
        return Err("Initial concentration must be in range 0..1".into());
    }

    let inflow_rate: f64 = get_input("Inflow rate")?.parse()?;

    if inflow_rate <= 0.0 {
        return Err("Inflow rate must be greater than 0.".into());
    }

    let target_concentration: f64 = get_input("Target concentration")?.parse()?;

    if target_concentration < 0.0 || target_concentration > 1.0 {
        return Err("Target concentration must be in range 0..1".into());
    }

    let t = calculate_time(
        volume,
        initial_concentration,
        inflow_rate,
        target_concentration,
    )?;

    // Round to nearest second and print result
    println!(
        "Time to reach {}% nitrogen: {} seconds",
        target_concentration * 100.0,
        t.round()
    );

    Ok(())
}

fn calculate_time(
    volume: f64,
    initial_concentration: f64,
    inflow_rate: f64,
    target_concentration: f64,
) -> Result<f64, Box<dyn Error>> {
    if target_concentration <= initial_concentration {
        return Err("Target concentration must be greater than initial.".into());
    }

    // Calculate derived values
    let initial_n2 = volume * initial_concentration;
    let target_n2 = volume * target_concentration;
    let steady_state_n2 = volume; // Maximum possible N2 at infinite time
    let k = inflow_rate / volume; // Rate constant

    // Calculate time using derived formula
    Ok(-(((steady_state_n2 - target_n2) / (steady_state_n2 - initial_n2)) as f64).ln() / k)
}

fn get_input(prompt: &str) -> io::Result<String> {
    println!("{}: ", prompt);
    let mut input = String::new();
    let _ = io::stdin().read_line(&mut input)?;
    Ok(input.trim().to_string())
}
